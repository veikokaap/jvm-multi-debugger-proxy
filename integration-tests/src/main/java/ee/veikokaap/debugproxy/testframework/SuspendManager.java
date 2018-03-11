package ee.veikokaap.debugproxy.testframework;

public class SuspendManager {
  private final DebuggerProcess parentDebugger;

  public SuspendManager(DebuggerProcess parentDebugger) {
    this.parentDebugger = parentDebugger;
  }

  public void resume() {
    parentDebugger.resume();
  }
}
