package ee.veikokaap.debugproxy.testframework;

public class BreakpointManager {
  private final DebuggerProcess parentDebugger;

  public BreakpointManager(DebuggerProcess parentDebugger) {
    this.parentDebugger = parentDebugger;
  }

  public void resume() {
    parentDebugger.resume();
  }
}
