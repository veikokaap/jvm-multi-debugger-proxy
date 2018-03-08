package ee.veikokaap.debugproxy.testframework;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
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

import ee.veikokaap.debugproxy.testframework.utils.BreakpointLocation;

public class DebuggerProcess {

  private final VirtualMachine virtualMachine;
  private final Map<BreakpointRequest, Set<Consumer<BreakpointManager>>> requestListenerMap = new ConcurrentHashMap<>();
  private final Map<BreakpointLocation, BreakpointRequest> locationRequestMap = new ConcurrentHashMap<>();

  private DebuggerProcess(VirtualMachine virtualMachine) {
    this.virtualMachine = virtualMachine;
    new Thread(() -> listenForEvents(virtualMachine)).start();
  }

  public static DebuggerProcess attach() throws IOException, IllegalConnectorArgumentsException {
    return new DebuggerProcess(connect());
  }

  private void listenForEvents(VirtualMachine virtualMachine) {
    while (!Thread.interrupted()) {
      try {
        virtualMachine.eventQueue().remove().stream()
            .map(Event::request)
            .filter(requestListenerMap.keySet()::contains)
            .map(requestListenerMap::get)
            .flatMap(Set::stream)
            .forEach(listener -> new Thread(() -> listener.accept(new BreakpointManager(this))).start());
      }
      catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public AsyncTester<BreakpointManager> createBreakRequestAt(BreakpointLocation location, Consumer<BreakpointManager> onBreakListener) throws AbsentInformationException {
    if (!locationRequestMap.containsKey(location)) {
      BreakpointRequest request = virtualMachine.eventRequestManager().createBreakpointRequest(findLocation(location));
      locationRequestMap.put(location, request);
      requestListenerMap.put(request, new HashSet<>());
    }

    BreakpointRequest request = locationRequestMap.get(location);
    AsyncTester<BreakpointManager> tester = new AsyncTester<>(onBreakListener, request);
    requestListenerMap.get(request).add(tester);

    return tester;
  }

  void resume() {
    virtualMachine.resume();
  }

  public void waitFor() throws InterruptedException {
    virtualMachine.process().waitFor();
  }

  private Location findLocation(BreakpointLocation location) throws AbsentInformationException {
    ReferenceType classRef = virtualMachine.allClasses().stream()
        .filter(c -> c.name().equals(location.getClassName()))
        .findFirst()
        .get();

    return classRef.allLineLocations().stream()
        .filter(loc -> loc.lineNumber() == location.getLineNumber())
        .findFirst()
        .get();
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

}
