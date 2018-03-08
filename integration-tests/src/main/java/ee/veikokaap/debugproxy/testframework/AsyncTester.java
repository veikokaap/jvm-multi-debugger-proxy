package ee.veikokaap.debugproxy.testframework;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import com.sun.jdi.request.BreakpointRequest;

public class AsyncTester<T> implements Consumer<T> {

  private final AtomicReference<AssertionError> exception = new AtomicReference<>(null);
  private final AtomicReference<Thread> thread = new AtomicReference<>(null);
  private final Consumer<T> consumer;
  private final BreakpointRequest request;

  AsyncTester(Consumer<T> consumer, BreakpointRequest request) {
    this.consumer = consumer;
    this.request = request;
  }

  @Override
  public void accept(T t) {
    thread.set(Thread.currentThread());
    try {
      consumer.accept(t);
    } catch (AssertionError e) {
      exception.set(e);
    }
  }

  public AsyncTester<T> enable() {
    request.enable();
    return this;
  }

  public void joinAndTest(long time, TimeUnit timeUnit) throws InterruptedException {
    long millis = timeUnit.toMillis(time);
    long start = System.currentTimeMillis();

    waitForConsumerRun(millis, start);
    if (thread.get() == null) {
      throw new AssertionError("Consumer hasn't been run in " + time + " " + timeUnit);
    }

    thread.get().join(timeLeft(millis, start));
    if (exception.get() != null) {
      throw exception.get();
    }
  }

  private long timeLeft(long millis, long start) {
    return millis - (System.currentTimeMillis() - start);
  }

  private void waitForConsumerRun(long millis, long start) throws InterruptedException {
    while (thread.get() == null && (System.currentTimeMillis() - start < millis)) {
      Thread.sleep(100);
    }
  }
}
