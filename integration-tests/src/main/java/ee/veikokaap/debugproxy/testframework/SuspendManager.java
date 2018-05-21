package ee.veikokaap.debugproxy.testframework;

import java.util.function.Consumer;

import org.checkerframework.checker.nullness.qual.Nullable;
import com.sun.jdi.event.BreakpointEvent;
import com.sun.jdi.event.Event;

public class SuspendManager {
  private final DebuggerProcess parentDebugger;
  private final Event event;

  SuspendManager(DebuggerProcess parentDebugger, Event event) {
    this.parentDebugger = parentDebugger;
    this.event = event;
  }

  void resume() {
    parentDebugger.resume();
  }

  void step(Consumer<SuspendManager> listener, int size, int depth) throws Throwable {
    BreakpointEvent event = (BreakpointEvent) this.event;
    AsyncTester tester = parentDebugger.step(event, listener, size, depth);
    resume();
    tester.joinAndTest();
  }

}
