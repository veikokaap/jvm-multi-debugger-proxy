package ee.veikokaap.debugproxy.tests

import ee.veikokaap.debugproxy.testframework.utils.BreakpointUtil
import org.junit.Test
import java.util.concurrent.TimeUnit

open class SingleDebuggerTests {

    val testClass = SimpleBreakpointClass::class.java
    val firstBreakpoint = BreakpointUtil.findBreakLocation(testClass, 0)
    val secondBreakpoint = BreakpointUtil.findBreakLocation(testClass, 1)

    @Test(timeout = 5000)
    fun `test a single breakpoint with a single debugger`() = runTest(testClass) { jvm, debugger ->
        jvm.outputDeque.assertContainsOnly("Listening for transport dt_socket at address: 16789")

        val breakpoint = debugger.breakAt(firstBreakpoint) { breakpoint ->
            jvm.outputDeque.assertContainsOnly("Before breakpoints")
            breakpoint.resume()
        }

        debugger.allBreakpointSet()

        breakpoint.joinAndTest(1, TimeUnit.SECONDS)
        jvm.waitForExit(1, TimeUnit.SECONDS)

        jvm.outputDeque.assertContainsOnly("After breakpoint 0", "After breakpoint 1")
    }

    @Test(timeout = 5000)
    fun `test 2 breakpoints with a single debugger`() = runTest(testClass) { jvm, debugger ->
        jvm.outputDeque.assertContainsOnly("Listening for transport dt_socket at address: 16789")

        val firstBreak = debugger.breakAt(firstBreakpoint) {
            jvm.outputDeque.assertContainsOnly("Before breakpoints")
            it.resume()
        }

        val secondBreak = debugger.breakAt(secondBreakpoint) {
            jvm.outputDeque.assertContainsOnly("After breakpoint 0")
            it.resume()
        }

        debugger.allBreakpointSet()

        firstBreak.joinAndTest(1, TimeUnit.SECONDS)
        secondBreak.joinAndTest(1, TimeUnit.SECONDS)
        jvm.waitForExit(1, TimeUnit.SECONDS)

        jvm.outputDeque.assertContainsOnly("After breakpoint 1")
    }

}
