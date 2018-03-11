package ee.veikokaap.debugproxy.tests

import ee.veikokaap.debugproxy.testframework.DebuggerProcess
import ee.veikokaap.debugproxy.testframework.JvmProcess
import java.util.*
import kotlin.test.assertEquals

fun Deque<String>.assertContainsOnly(vararg lines: String) {
    lines.forEach{line -> assertEquals(line, this.pollFirst())}
    assertEquals(null, this.pollFirst())
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