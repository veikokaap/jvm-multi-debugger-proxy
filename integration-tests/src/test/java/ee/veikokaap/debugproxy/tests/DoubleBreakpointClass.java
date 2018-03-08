package ee.veikokaap.debugproxy.tests;

import ee.veikokaap.debugproxy.testframework.utils.BreakpointUtil;

public class DoubleBreakpointClass {
  public static void main(String[] args) throws InterruptedException {
    // Give time for breakpoints to attach
    Thread.sleep(2000);

    System.out.println("Before");
    BreakpointUtil.mark(0);
    System.out.println("Between");
    BreakpointUtil.mark(1);
    System.out.println("After");
  }
}
