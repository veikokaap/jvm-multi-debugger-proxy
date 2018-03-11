package ee.veikokaap.debugproxy.tests;

import ee.veikokaap.debugproxy.testframework.utils.BreakpointUtil;

public class SingleBreakpointClass {
  public static void main(String[] args) throws InterruptedException {
    System.out.println("Before");
    BreakpointUtil.mark(0);
    System.out.println("After");
  }
}
