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
import com.sun.jdi.event.BreakpointEvent;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.EventSet;
import com.sun.jdi.request.BreakpointRequest;
import com.sun.jdi.request.ClassPrepareRequest;
import com.sun.jdi.request.EventRequest;
import com.sun.jdi.request.StepRequest;

import ee.veikokaap.debugproxy.testframework.utils.BreakpointLocation;

public class DebuggerProcess implements AutoCloseable {

  private final VirtualMachine virtualMachine;
  private final Map<EventRequestIdentifier, List<Consumer<SuspendManager>>> requestListenerMap = new ConcurrentHashMap<>();

  private final Thread thread;
  private final List<Thread> spawnedThreads = Collections.synchronizedList(new ArrayList<>());
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
        getEvents(virtualMachine).forEach(this::triggerSuspendEvent);
      }
      catch (InterruptedException e) {
        markFailure(e);
      }
    }
  }

  private List<Event> getEvents(VirtualMachine virtualMachine) throws InterruptedException {
    EventSet set = virtualMachine.eventQueue().remove(10);
    if (set == null) {
      return Collections.emptyList();
    }
    else {
      return set.stream()
          .filter(Objects::nonNull)
          .collect(Collectors.toList());
    }
  }

  private void triggerSuspendEvent(Event event) {
    if (event.request() == null) {
      return;
    }

    List<Consumer<SuspendManager>> listeners;
    try {
      EventRequestIdentifier key = EventRequestIdentifier.fromEventRequest(event.request());
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

    if (event.request() instanceof StepRequest) {
      event.request().disable();
    }

    CountDownLatch resumeLatch = new CountDownLatch(listeners.size());
    for (Consumer<SuspendManager> listener : listeners) {
      Thread thread = new Thread(() -> {
        listener.accept(new SuspendManager(this, event));
        resumeLatch.countDown();
      });
      thread.setDaemon(true);
      spawnedThreads.add(thread);
      thread.start();
    }

    if (event.request() instanceof ClassPrepareRequest) {
      try {
        resumeLatch.await();
        resume();
      }
      catch (InterruptedException e) {
        markFailure(e);
      }
    }

  }

  private void markFailure(Exception e) {
    exception.set(e);
  }

  public SuspendManagerAsyncTester breakAt(BreakpointLocation breakpointLocation, Consumer<SuspendManager> onBreakListener) {
    ClassPrepareRequest classPrepareRequest = virtualMachine.eventRequestManager().createClassPrepareRequest();
    classPrepareRequest.addClassFilter(breakpointLocation.getClassName());
    classPrepareRequest.enable();

    SuspendManagerAsyncTester tester = new SuspendManagerAsyncTester(onBreakListener);

    try {
      registerRequest(classPrepareRequest, manager -> addBreakpoint(breakpointLocation, tester));
    }
    catch (ReflectiveOperationException e) {
      markFailure(e);
    }

    return tester;
  }

  private void addBreakpoint(BreakpointLocation breakpointLocation, SuspendManagerAsyncTester tester) {
    try {
      Optional<Location> loc = findLocation(breakpointLocation);
      if (!loc.isPresent()) {
        throw new AssertionError("Failed to find location");
      }
      BreakpointRequest breakPointRequest = virtualMachine.eventRequestManager().createBreakpointRequest(loc.get());
      breakPointRequest.enable();

      registerRequest(breakPointRequest, tester);
    }
    catch (Exception e) {
      tester.failTestWithException(e);
    }
  }

  private void registerRequest(EventRequest request, Consumer<SuspendManager> listener) throws ReflectiveOperationException {
    EventRequestIdentifier identifier = EventRequestIdentifier.fromEventRequest(request);
    requestListenerMap.putIfAbsent(identifier, new ArrayList<>());
    requestListenerMap.get(identifier).add(listener);
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
      spawnedThreads.forEach(Thread::interrupt);
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

  public SuspendManagerAsyncTester step(BreakpointEvent event, Consumer<SuspendManager> listener, int size, int depth) throws ReflectiveOperationException {
    SuspendManagerAsyncTester tester = new SuspendManagerAsyncTester(listener);

    StepRequest request = virtualMachine.eventRequestManager().createStepRequest(event.thread(), size, depth);
    request.addCountFilter(1);
    request.enable();
    registerRequest(request, tester);

    return tester;
  }
}
