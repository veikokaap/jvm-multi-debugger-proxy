package ee.veikokaap.debugproxy.testframework.utils;

import java.util.HashMap;
import java.util.Map;
import org.checkerframework.checker.nullness.qual.KeyFor;

public class BreakpointUtil {

  private static final Map<Class, Map<Integer, BreakpointLocation>> cache = new HashMap<>();

  /**
   * Method that's only used for later placing breakpoints on the same line
   * @param markerId from 0 to 5
   */
  public static void mark(int markerId) {
  }

  private static Map<Integer, BreakpointLocation> getCacheForClass(Class clazz) {
    if (!cache.containsKey(clazz)) {
      cache.put(clazz, MarkerFinder.findLocations(clazz));
    }
    return cache.get(clazz);
  }

  public static BreakpointLocation findBreakLocation(Class clazz, Integer id) {
    Map<Integer, BreakpointLocation> cacheForClass = getCacheForClass(clazz);

    if (cacheForClass.containsKey(id)) {
      return cacheForClass.get(id);
    }
    else {
      throw new AssertionError("Problems finding a breakpoint location.");
    }
  }

}
