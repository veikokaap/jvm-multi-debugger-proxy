package ee.veikokaap.debugproxy.tests

import ee.veikokaap.debugproxy.testframework.utils.BreakpointUtil
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.empty
import org.junit.Assert.assertThat
import org.junit.Test
import java.util.concurrent.TimeUnit
import kotlin.test.assertTrue

open class DoubleDebuggerTests {

    val testClass = SimpleBreakpointClass::class.java
    val firstLocation = BreakpointUtil.findBreakLocation(testClass, 0)
    val secondLocation = BreakpointUtil.findBreakLocation(testClass, 1)

    @Test
    fun `test single breakpoint with 2 debuggers`() = runTest(testClass, 10, TimeUnit.SECONDS) { jvm, firstDebugger, secondDebugger ->
        jvm.outputDeque.assertContainsOnly("Listening for transport dt_socket at address: 16789")

        val firstBreak = firstDebugger.breakAt(firstLocation) {
            jvm.outputDeque.assertContainsOnly("Before breakpoints")
            it.resume()
        }

        val secondBreak = secondDebugger.breakAt(firstLocation) {
            firstBreak.joinAndTest(4, TimeUnit.SECONDS)
            assertThat(jvm.outputDeque, `is`(empty()))
            assertTrue(jvm.outputDeque.isEmpty())
            it.resume()
        }

        firstDebugger.allBreakpointSet()

        secondBreak.joinAndTest(4, TimeUnit.SECONDS)
        jvm.waitForExit(4, TimeUnit.SECONDS)

        jvm.outputDeque.assertContainsOnly("After breakpoint 0", "After breakpoint 1")
    }

    @Test
    fun `test 2 breakpoints with 2 debuggers`() = runTest(testClass, 10, TimeUnit.SECONDS) { jvm, firstDebugger, secondDebugger ->
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
        secondDebugger.allBreakpointSet()

        firstBreak.joinAndTest(5, TimeUnit.SECONDS)
        secondBreak.joinAndTest(5, TimeUnit.SECONDS)
        jvm.waitForExit(5, TimeUnit.SECONDS)

        jvm.outputDeque.assertContainsOnly("After breakpoint 1")
    }


}