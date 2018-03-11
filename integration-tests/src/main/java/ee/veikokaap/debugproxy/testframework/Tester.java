package ee.veikokaap.debugproxy.testframework;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public interface Tester<T> extends Consumer<T> {
  void joinAndTest(long time, TimeUnit timeUnit) throws Throwable;
}
