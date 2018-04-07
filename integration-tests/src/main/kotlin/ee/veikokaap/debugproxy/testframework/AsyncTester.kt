package ee.veikokaap.debugproxy.testframework

import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import java.util.concurrent.atomic.AtomicReference
import java.util.function.Consumer

open class AsyncTester<T>(private val consumer: Consumer<T>): Consumer<T> {

    protected val exception = AtomicReference<Throwable>(null)
    private val thread = AtomicReference<Thread>(null)

    override fun accept(t: T) {
        if (exception.get() != null) {
            return  // Test previously failed
        }

        thread.set(Thread.currentThread())
        try {
            consumer.accept(t)
        } catch (e: Throwable) {
            exception.set(e)
        }
    }

    @Throws(Throwable::class)
    fun joinAndTest() {
        joinAndTest(Long.MAX_VALUE, TimeUnit.DAYS);
    }

    @Throws(Throwable::class)
    fun joinAndTest(time: Long, timeUnit: TimeUnit) {
        val millis = timeUnit.toMillis(time)
        val start = System.currentTimeMillis()

        waitForConsumerRun(millis, start)
        if (thread.get() == null) {
            throw AssertionError("Consumer hasn't been run in $time $timeUnit")
        }

        val timeLeft = timeLeft(millis, start)
        if (timeLeft <= 0) {
            throwTimeoutException(time, timeUnit)
        }
        thread.get().join(timeLeft)

        if (thread.get().isAlive) {
            throwTimeoutException(time, timeUnit)
        }
        if (exception.get() != null) {
            throw exception.get()
        }
    }

    private fun throwTimeoutException(time: Long, timeUnit: TimeUnit) {
        thread.get().interrupt()
        val timeoutException = TimeoutException("Thread should have finished work in $time $timeUnit")
        if (exception.get() != null) {
            timeoutException.addSuppressed(exception.get())
        }
        throw timeoutException
    }

    private fun timeLeft(millis: Long, start: Long) = millis - (System.currentTimeMillis() - start)

    @Throws(InterruptedException::class)
    private fun waitForConsumerRun(millis: Long, start: Long) {
        while (thread.get() == null && System.currentTimeMillis() - start < millis) {
            Thread.sleep(100)
        }
    }

    fun failTestWithException(e: Exception) = exception.set(e)

}
