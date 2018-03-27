package ee.veikokaap.debugproxy.tests

import ee.veikokaap.debugproxy.testframework.AsyncTester
import ee.veikokaap.debugproxy.testframework.DebuggerProcess
import ee.veikokaap.debugproxy.testframework.JvmProcess
import ee.veikokaap.debugproxy.testframework.SuspendManager
import org.hamcrest.Matchers
import org.junit.Assert
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.function.Consumer
import kotlin.test.assertEquals

fun Deque<String>.assertContainsOnly(vararg lines: String) {
    lines.forEach { line -> assertEquals(line, this.pollFirst()) }
    Assert.assertThat(this, Matchers.`is`(Matchers.empty()))
}

fun runTest(testClass: Class<*>, timeout: Long, timeUnit: TimeUnit, test: (JvmProcess, DebuggerProcess) -> Unit) {
    JvmProcess.runClass(testClass).use { jvm ->
        DebuggerProcess.attach().use { debugger ->
            val tester = asyncTester<Unit?> { test(jvm, debugger) }
            Thread { tester.accept(null) }.start()
            tester.joinAndTest(timeout, timeUnit)
        }
    }
}

fun runTest(testClass: Class<*>, timeout: Long, timeUnit: TimeUnit, test: (JvmProcess, DebuggerProcess, DebuggerProcess) -> Unit) {
    JvmProcess.runClass(testClass).use { jvm ->
        DebuggerProcess.attach().use { firstDebugger ->
            DebuggerProcess.attach().use { secondDebugger ->
                val tester = asyncTester<Unit?> { test(jvm, firstDebugger, secondDebugger) }
                Thread { tester.accept(null) }.start()
                tester.joinAndTest(timeout, timeUnit)
            }
        }
    }
}

fun <T> asyncTester(f: (T) -> Unit) = AsyncTester(object : Consumer<T> {
    override fun accept(t: T) {
        f(t)
    }
})

fun toRunnable(f: () -> Unit): Runnable = object : Runnable {
    override fun run() {
        f()
    }
}