package ee.veikokaap.debugproxy.tests.singledebugger

import ee.veikokaap.debugproxy.testframework.utils.BreakpointUtil
import ee.veikokaap.debugproxy.tests.LoopBreakpointClass
import ee.veikokaap.debugproxy.tests.assertContainsOnly
import ee.veikokaap.debugproxy.tests.runTest
import org.junit.Test
import java.util.concurrent.TimeUnit

open class LoopSingleDebuggerTests {

    val testClass = LoopBreakpointClass::class.java
    val breakpoint = BreakpointUtil.findBreakLocation(testClass, 0)

    @Test
    fun `test a single breakpoint in for loop with a single debugger`() = runTest(testClass, 10, TimeUnit.SECONDS) { jvm, debugger ->
        jvm.outputDeque.assertContainsOnly("Listening for transport dt_socket at address: 16789")

        var count = 0;
        val breakpoint = debugger.breakAt(breakpoint) { breakpoint ->
            if (count == 0) {
                jvm.outputDeque.assertContainsOnly(LoopBreakpointClass.BEFORE_MESSAGE, LoopBreakpointClass.getBreakpointMessage(0))
            }
            else {
                jvm.outputDeque.assertContainsOnly(LoopBreakpointClass.getBreakpointMessage(count))
            }
            count++;
            breakpoint.resume()
        }

        debugger.allBreakpointSet()

        breakpoint.joinAndTest(2, TimeUnit.SECONDS)
        jvm.waitForExit(2, TimeUnit.SECONDS)

        jvm.outputDeque.assertContainsOnly(LoopBreakpointClass.AFTER_MESSAGE)
    }

}
