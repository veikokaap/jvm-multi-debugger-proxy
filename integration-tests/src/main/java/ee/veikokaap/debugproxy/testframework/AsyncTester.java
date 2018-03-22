package ee.veikokaap.debugproxy.testframework;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class AsyncTester<T> implements Tester<T> {

  private final AtomicReference<Throwable> exception = new AtomicReference<>(null);
  private final AtomicReference<Thread> thread = new AtomicReference<>(null);
  private final Consumer<T> consumer;

  public AsyncTester(Consumer<T> consumer) {
    this.consumer = consumer;
  }

  @Override
  public void accept(T t) {
    if (exception.get() != null) {
      return; // Test previously failed
    }

    thread.set(Thread.currentThread());
    try {
      consumer.accept(t);
    }
    catch (AssertionError e) {
      exception.set(e);
    }
  }

  @Override
  public void joinAndTest(long time, TimeUnit timeUnit) throws Throwable {
    long millis = timeUnit.toMillis(time);
    long start = System.currentTimeMillis();

    waitForConsumerRun(millis, start);
    if (thread.get() == null) {
      throw new AssertionError("Consumer hasn't been run in " + time + " " + timeUnit);
    }
    thread.get().join(timeLeft(millis, start));

    if (thread.get().isAlive()) {
      thread.get().interrupt();
      TimeoutException timeoutException = new TimeoutException("Thread should have finished work in " + time + " " + timeUnit);
      if (exception.get() != null) {
        timeoutException.addSuppressed(exception.get());
      }
      throw timeoutException;
    }
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

  public void failTestWithException(Exception e) {
    exception.set(e);
  }
}
