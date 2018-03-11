package ee.veikokaap.debugproxy.tests;

import ee.veikokaap.debugproxy.testframework.utils.BreakpointUtil;

public class SimpleBreakpointClass {
  public static void main(String[] args) {
    System.out.println("Before breakpoints");
    BreakpointUtil.mark(0);
    System.out.println("After breakpoint 0");
    BreakpointUtil.mark(1);
    System.out.println("After breakpoint 1");
  }
}
