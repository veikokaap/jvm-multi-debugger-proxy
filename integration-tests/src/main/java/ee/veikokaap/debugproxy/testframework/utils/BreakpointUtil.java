package ee.veikokaap.debugproxy.testframework.utils;

import java.util.HashMap;
import java.util.Map;

public class BreakpointUtil {

  private static final Map<Class, HashMap<Integer, BreakpointLocation>> cache = new HashMap<>();

  /**
   * Method that's only used for later placing breakpoints on the same line
   * @param markerId from 0 to 5
   */
  public static void mark(int markerId) {
  }

  public static BreakpointLocation findBreakLocation(Class clazz, int id) {
    if (!cache.containsKey(clazz)) {
      cache.put(clazz, MarkerFinder.findLocations(clazz));
    }

    return cache.get(clazz).get(id);
  }

}
