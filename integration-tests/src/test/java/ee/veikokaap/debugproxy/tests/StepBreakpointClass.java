package ee.veikokaap.debugproxy.tests;

import ee.veikokaap.debugproxy.testframework.utils.BreakpointUtil;

public class StepBreakpointClass {

  public static final String BEFORE_MESSAGE = "Before breakpoints";
  public static final String AFTER_BREAKPOINT_0 = "After breakpoint 0";
  public static final String STEPPED_OVER = "After step from breakpoint 0";
  public static final String AFTER_BREAKPOINT_1 = "After breakpoint 1";
  public static final String LAST_MESSAGE = "After breakpoints";

  public static void main(String[] args) {
    System.out.println(BEFORE_MESSAGE);
    System.out.println(AFTER_BREAKPOINT_0); BreakpointUtil.mark(0);
    System.out.println(STEPPED_OVER);
    System.out.println(AFTER_BREAKPOINT_1); BreakpointUtil.mark(1);
    System.out.println(LAST_MESSAGE);
  }
}
