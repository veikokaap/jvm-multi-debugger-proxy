package ee.veikokaap.debugproxy.testframework;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.Bootstrap;
import com.sun.jdi.Location;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.connect.AttachingConnector;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.EventSet;
import com.sun.jdi.request.BreakpointRequest;
import com.sun.jdi.request.ClassPrepareRequest;
import com.sun.jdi.request.EventRequest;

import ee.veikokaap.debugproxy.testframework.utils.BreakpointLocation;

public class DebuggerProcess implements AutoCloseable {

  private final VirtualMachine virtualMachine;
  private final Map<EventRequestIdentifier, List<Consumer<SuspendManager>>> requestListenerMap = new ConcurrentHashMap<>();

  private final Thread thread;
  private final AtomicReference<Exception> exception = new AtomicReference<>();
  private final AtomicBoolean running = new AtomicBoolean(true);

  private DebuggerProcess(VirtualMachine virtualMachine) {
    this.virtualMachine = virtualMachine;
    thread = new Thread(() -> listenForEvents(virtualMachine));
    thread.start();
  }

  public static DebuggerProcess attach() throws IOException, IllegalConnectorArgumentsException {
    return new DebuggerProcess(connect());
  }

  private void listenForEvents(VirtualMachine virtualMachine) {
    while (running.get() && !Thread.currentThread().isInterrupted()) {
      try {
        getEventRequests(virtualMachine).forEach(this::triggerSuspendEvent);
      }
      catch (InterruptedException e) {
        markFailure(e);
      }
    }
  }

  private List<EventRequest> getEventRequests(VirtualMachine virtualMachine) throws InterruptedException {
    EventSet set = virtualMachine.eventQueue().remove(10);
    if (set == null) {
      return Collections.emptyList();
    }
    else {
      return set.stream()
          .map(Event::request)
          .filter(Objects::nonNull)
          .collect(Collectors.toList());
    }
  }

  private void triggerSuspendEvent(EventRequest eventRequest) {
    List<Consumer<SuspendManager>> listeners;
    try {
      EventRequestIdentifier key = EventRequestIdentifier.fromEventRequest(eventRequest);
      System.out.println("XXX3: " + key + " -> " + key.hashCode());
      listeners = requestListenerMap.get(key);
    }
    catch (ReflectiveOperationException e) {
      markFailure(e);
      return;
    }

    if (listeners == null) {
      try {
        throw new Exception("Listeners entry null");
      }
      catch (Exception e) {
        markFailure(e);
        return;
      }
    }

    CountDownLatch resumeLatch = new CountDownLatch(listeners.size());
    for (Consumer<SuspendManager> listener : listeners) {
      new Thread(() -> {
        listener.accept(new SuspendManager(this));
        resumeLatch.countDown();
      }).start();
    }

    try {
      if (eventRequest instanceof ClassPrepareRequest) {
        resumeLatch.await();
        resume();
      }
    }
    catch (InterruptedException e) {
      markFailure(e);
    }
  }

  private void markFailure(Exception e) {
    exception.set(e);
  }

  public AsyncTester<SuspendManager> breakAt(BreakpointLocation breakpointLocation, Consumer<SuspendManager> onBreakListener) {
    ClassPrepareRequest classPrepareRequest = virtualMachine.eventRequestManager().createClassPrepareRequest();
    classPrepareRequest.addClassFilter(breakpointLocation.getClassName());
    classPrepareRequest.enable();

    AsyncTester<SuspendManager> tester = new AsyncTester<>(onBreakListener);

    try {
      EventRequestIdentifier identifier = EventRequestIdentifier.fromEventRequest(classPrepareRequest);
      System.out.println("XXX1: " + identifier + " -> " + identifier.hashCode());
      requestListenerMap.putIfAbsent(identifier, new ArrayList<>());
      requestListenerMap.get(identifier).add(manager -> addBreakpoint(breakpointLocation, tester));
    }
    catch (ReflectiveOperationException e) {
      markFailure(e);
    }

    return tester;
  }

  private void addBreakpoint(BreakpointLocation breakpointLocation, AsyncTester<SuspendManager> tester) {
    try {
      Optional<Location> loc = findLocation(breakpointLocation);
      if (!loc.isPresent()) {
        throw new AssertionError("Failed to find location");
      }
      BreakpointRequest breakPointRequest = virtualMachine.eventRequestManager().createBreakpointRequest(loc.get());
      breakPointRequest.enable();

      EventRequestIdentifier identifier = EventRequestIdentifier.fromEventRequest(breakPointRequest);
      requestListenerMap.putIfAbsent(identifier, new ArrayList<>());
      requestListenerMap.get(identifier).add(tester);
    }
    catch (Exception e) {
      tester.failTestWithException(e);
    }
  }

  public void allBreakpointSet() {
    resume();
  }

  void resume() {
    virtualMachine.resume();
  }

  private Optional<Location> findLocation(BreakpointLocation location) throws AbsentInformationException {
    Optional<ReferenceType> classRef = virtualMachine.allClasses().stream()
        .filter(c -> c.name().equals(location.getClassName()))
        .findFirst();

    if (!classRef.isPresent()) {
      return Optional.empty();
    }
    else {
      return classRef.get().allLineLocations().stream()
          .filter(loc -> loc.lineNumber() == location.getLineNumber())
          .findFirst();
    }
  }

  private static VirtualMachine connect() throws IOException, IllegalConnectorArgumentsException {
    Connector connector = getConnector();

    Map<String, Connector.Argument> arguments = connector.defaultArguments();
    arguments.get("hostname").setValue("localhost");
    arguments.get("port").setValue(String.valueOf(JvmProcess.PROXY_PORT));

    return ((AttachingConnector) connector).attach(arguments);
  }

  private static Connector getConnector() {
    return Bootstrap.virtualMachineManager().allConnectors().stream()
        .filter(c -> c.name().equals("com.sun.jdi.SocketAttach"))
        .findFirst()
        .get();
  }

  @Override
  public void close() throws Exception {
    running.set(false);
    try {
      thread.join(100);
    }
    catch (InterruptedException e) {
      markFailure(e);
      thread.interrupt();
    }
    finally {
      checkForExceptions();
    }
  }

  private void checkForExceptions() throws Exception {
    if (exception.get() != null) {
      throw exception.get();
    }
  }
}
