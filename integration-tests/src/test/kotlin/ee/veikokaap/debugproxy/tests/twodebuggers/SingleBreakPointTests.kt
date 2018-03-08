package ee.veikokaap.debugproxy.tests.twodebuggers

import ee.veikokaap.debugproxy.testframework.DebuggerProcess
import ee.veikokaap.debugproxy.testframework.JvmProcess
import ee.veikokaap.debugproxy.testframework.utils.BreakpointUtil
import ee.veikokaap.debugproxy.tests.SingleBreakpointClass
import org.junit.Test
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals

class SingleBreakPointTests {

    val testClass = SingleBreakpointClass::class.java
    val location = BreakpointUtil.findBreakLocation(testClass, 0)

    @Test
    fun `test single breakpoint with 2 debuggers`() {
        val jvm = JvmProcess.runClass(testClass)
        val debugger = DebuggerProcess.attach()

        val firstBreak = debugger.breakAt(location) {
            assertEquals("Listening for transport dt_socket at address: 16789", jvm.outputDeque.pollFirst())
            assertEquals("Before", jvm.outputDeque.pollFirst())
            assertEquals(null, jvm.outputDeque.pollFirst())
            it.resume()
        }

        val secondBreak = debugger.breakAt(location) {
            firstBreak.joinAndTest(2, TimeUnit.SECONDS)
            assertEquals(null, jvm.outputDeque.pollFirst())
            it.resume()
        }

        firstBreak.enable()
        secondBreak.enable()
        secondBreak.joinAndTest(5, TimeUnit.SECONDS)

        jvm.waitForExit(2, TimeUnit.SECONDS)

        assertEquals("After", jvm.outputDeque.pollFirst())
        assertEquals(null, jvm.outputDeque.pollFirst())
    }
}