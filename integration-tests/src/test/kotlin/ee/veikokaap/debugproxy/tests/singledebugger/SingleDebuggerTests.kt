package ee.veikokaap.debugproxy.tests.singledebugger

import ee.veikokaap.debugproxy.testframework.utils.BreakpointUtil
import ee.veikokaap.debugproxy.tests.SimpleBreakpointClass
import ee.veikokaap.debugproxy.tests.assertContainsOnly
import ee.veikokaap.debugproxy.tests.runTest
import org.junit.Test
import java.util.concurrent.TimeUnit

open class SingleDebuggerTests {

    val testClass = SimpleBreakpointClass::class.java
    val firstBreakpoint = BreakpointUtil.findBreakLocation(testClass, 0)
    val secondBreakpoint = BreakpointUtil.findBreakLocation(testClass, 1)

    @Test
    fun `test a single breakpoint with a single debugger`() = runTest(testClass, 10, TimeUnit.SECONDS) { jvm, debugger ->
        jvm.outputDeque.assertContainsOnly("Listening for transport dt_socket at address: 16789")

        val breakpoint = debugger.breakAt(firstBreakpoint) { breakpoint ->
            jvm.outputDeque.assertContainsOnly("Before breakpoints")
            breakpoint.resume()
        }

        debugger.allBreakpointSet()

        breakpoint.joinAndTest(2, TimeUnit.SECONDS)
        jvm.waitForExit(2, TimeUnit.SECONDS)

        jvm.outputDeque.assertContainsOnly("After breakpoint 0", "After breakpoint 1")
    }

    @Test
    fun `test 2 breakpoints with a single debugger`() = runTest(testClass, 10, TimeUnit.SECONDS) { jvm, debugger ->
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

        firstBreak.joinAndTest(4, TimeUnit.SECONDS)
        secondBreak.joinAndTest(4, TimeUnit.SECONDS)
        jvm.waitForExit(4, TimeUnit.SECONDS)

        jvm.outputDeque.assertContainsOnly("After breakpoint 1")
    }

}
