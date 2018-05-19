package ee.veikokaap.debugproxy.tests

import ee.veikokaap.debugproxy.testframework.AsyncTester
import ee.veikokaap.debugproxy.testframework.DebuggerProcess
import ee.veikokaap.debugproxy.testframework.JvmProcess
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.function.Consumer
import kotlin.test.assertEquals
import kotlin.test.assertTrue

fun Deque<String>.assertAddedOutput(vararg lines: String, remove: Boolean = true) {
    val expectedList = lines.toList()

    synchronized(this) {
        val actualPolledList = this.toList();
        if (remove) {
            this.clear()
        }
        assertEquals(expectedList, actualPolledList)
    }
}

fun Deque<String>.assertNoOutputAdded() = assertTrue(this.isEmpty())

fun runTest(testClass: Class<*>, timeout: Long = 10, timeUnit: TimeUnit = TimeUnit.SECONDS, test: (JvmProcess, DebuggerProcess) -> Unit) {
    JvmProcess.runClass(testClass).use { jvm ->
        DebuggerProcess.attach().use { debugger ->
            val tester = asyncTester<Unit?> {
                assertEquals("Listening for transport dt_socket at address: ${JvmProcess.DEBUGGER_PORT}", jvm.outputDeque.pollLast())
                test(jvm, debugger)
            }
            Thread { tester.accept(null) }.start()
            tester.joinAndTest(timeout, timeUnit)
        }
    }
}

fun runTest(testClass: Class<*>, timeout: Long = 10, timeUnit: TimeUnit = TimeUnit.SECONDS, test: (JvmProcess, DebuggerProcess, DebuggerProcess) -> Unit) {
    JvmProcess.runClass(testClass).use { jvm ->
        DebuggerProcess.attach().use { firstDebugger ->
            DebuggerProcess.attach().use { secondDebugger ->
                val tester = asyncTester<Unit?> {
                    assertEquals("Listening for transport dt_socket at address: ${JvmProcess.DEBUGGER_PORT}", jvm.outputDeque.pollLast())
                    test(jvm, firstDebugger, secondDebugger)
                }
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