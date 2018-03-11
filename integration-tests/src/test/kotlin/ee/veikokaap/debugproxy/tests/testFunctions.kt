package ee.veikokaap.debugproxy.tests

import ee.veikokaap.debugproxy.testframework.DebuggerProcess
import ee.veikokaap.debugproxy.testframework.JvmProcess
import org.hamcrest.Matchers
import org.junit.Assert
import java.util.*
import kotlin.test.assertEquals

fun Deque<String>.assertContainsOnly(vararg lines: String) {
    lines.forEach{line -> assertEquals(line, this.pollFirst())}
    Assert.assertThat(this, Matchers.`is`(Matchers.empty()))
}

inline fun runTest(testClass: Class<*>, test: (JvmProcess, DebuggerProcess) -> Unit) {
    JvmProcess.runClass(testClass).use { jvm ->
        DebuggerProcess.attach().use { debugger ->
            test(jvm, debugger)
        }
    }
}

inline fun runTest(testClass: Class<*>, test: (JvmProcess, DebuggerProcess, DebuggerProcess) -> Unit) {
    JvmProcess.runClass(testClass).use { jvm ->
        DebuggerProcess.attach().use { firstDebugger ->
            DebuggerProcess.attach().use { secondDebugger ->
                test(jvm, firstDebugger, secondDebugger)
            }
        }
    }
}