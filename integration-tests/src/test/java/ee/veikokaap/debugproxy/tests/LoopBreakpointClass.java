package ee.veikokaap.debugproxy.tests;

import ee.veikokaap.debugproxy.testframework.utils.BreakpointUtil;

public class LoopBreakpointClass {

  public static final String BEFORE_MESSAGE = "Before breakpoints";
  public static final String BREAKPOINT_MESSAGE_PREFIX = "Breakpoint: ";
  public static final String AFTER_MESSAGE = "After breakpoints";

  public static String getBreakpointMessage(int breakCount) {
    return BREAKPOINT_MESSAGE_PREFIX + breakCount;
  }

  public static void main(String[] args) {
    System.out.println(BEFORE_MESSAGE);
    for (int i = 0; i < 10; i++) {
      System.out.println(getBreakpointMessage(i));
      BreakpointUtil.mark(0);
    }
    System.out.println(AFTER_MESSAGE);
  }
}
