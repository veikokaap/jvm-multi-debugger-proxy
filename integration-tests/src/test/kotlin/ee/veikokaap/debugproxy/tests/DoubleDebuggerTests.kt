package ee.veikokaap.debugproxy.tests

import ee.veikokaap.debugproxy.testframework.utils.BreakpointUtil
import org.junit.Test
import java.util.concurrent.TimeUnit

open class DoubleDebuggerTests {

    val testClass = SimpleBreakpointClass::class.java
    val firstLocation = BreakpointUtil.findBreakLocation(testClass, 0)
    val secondLocation = BreakpointUtil.findBreakLocation(testClass, 1)

    @Test(timeout = 5000)
    fun `test 2 breakpoints with 2 debuggers`() = runTest(testClass) { jvm, firstDebugger, secondDebugger ->
        jvm.outputDeque.assertContainsOnly("Listening for transport dt_socket at address: 16789")

        val firstBreak = firstDebugger.breakAt(firstLocation) {
            jvm.outputDeque.assertContainsOnly("Before breakpoints")
            it.resume()
        }

        val secondBreak = secondDebugger.breakAt(secondLocation) {
            jvm.outputDeque.assertContainsOnly("After breakpoint 0")
            it.resume()
        }

        firstDebugger.allBreakpointSet()

        firstBreak.joinAndTest(1, TimeUnit.SECONDS)
        secondBreak.joinAndTest(1, TimeUnit.SECONDS)
        jvm.waitForExit(1, TimeUnit.SECONDS)

        jvm.outputDeque.assertContainsOnly("After breakpoint 1")
    }


}