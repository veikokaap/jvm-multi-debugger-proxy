package ee.veikokaap.debugproxy.tests.singledebugger

import ee.veikokaap.debugproxy.testframework.DebuggerProcess
import ee.veikokaap.debugproxy.testframework.JvmProcess
import ee.veikokaap.debugproxy.testframework.utils.BreakpointUtil
import ee.veikokaap.debugproxy.tests.SingleBreakpointClass

abstract class SingleDebuggerTestBase {
    val testClass = SingleBreakpointClass::class.java
    val breakpointLocation = BreakpointUtil.findBreakLocation(testClass, 0)

    inline fun runTest(test: (JvmProcess, DebuggerProcess) -> Unit) {
        JvmProcess.runClass(testClass).use { jvm ->
            DebuggerProcess.attach().use { debugger ->
                test(jvm, debugger)
            }
        }
    }
}