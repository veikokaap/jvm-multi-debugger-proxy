package ee.veikokaap.debugproxy.testframework;

import java.util.function.Consumer;
import org.jetbrains.annotations.Nullable;

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

  void stepOver(@Nullable Consumer<SuspendManager> listener) throws Throwable {
    BreakpointEvent event = (BreakpointEvent) this.event;
    AsyncTester tester = parentDebugger.stepOver(event, listener);
    resume();
    tester.joinAndTest();
  }

  void stepInto(@Nullable Consumer<SuspendManager> listener) throws Throwable {
    BreakpointEvent event = (BreakpointEvent) this.event;
    AsyncTester tester = parentDebugger.stepInto(event, listener);
    resume();
    tester.joinAndTest();
  }
}
