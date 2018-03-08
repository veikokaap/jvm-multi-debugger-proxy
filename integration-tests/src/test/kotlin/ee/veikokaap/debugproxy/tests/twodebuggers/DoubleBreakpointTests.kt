package ee.veikokaap.debugproxy.tests.twodebuggers

import ee.veikokaap.debugproxy.testframework.DebuggerProcess
import ee.veikokaap.debugproxy.testframework.JvmProcess
import ee.veikokaap.debugproxy.testframework.utils.BreakpointUtil
import ee.veikokaap.debugproxy.tests.SingleBreakpointClass
import org.junit.Test
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals

open class DoubleBreakpointTests {

    val testClass = SingleBreakpointClass::class.java
    val firstLocation = BreakpointUtil.findBreakLocation(testClass, 0)
    val secondLocation = BreakpointUtil.findBreakLocation(testClass, 1)

    @Test
    fun `break, continue, break, continue`() {
        val jvm = JvmProcess.runClass(testClass)
        val firstDebugger = DebuggerProcess.attach()
        val secondDebugger = DebuggerProcess.attach()

        val firstBreak = firstDebugger.breakAt(firstLocation) {
            assertEquals("Listening for transport dt_socket at address: 16789", jvm.outputDeque.pollFirst())
            assertEquals("Before", jvm.outputDeque.pollFirst())
            assertEquals(null, jvm.outputDeque.pollFirst())
            it.resume()
        }

        val secondBreak = secondDebugger.breakAt(secondLocation) {
            assertEquals("Between", jvm.outputDeque.pollFirst())
            assertEquals(null, jvm.outputDeque.pollFirst())
            it.resume()
        }

        firstBreak.enable()
        secondBreak.enable()
        firstBreak.joinAndTest(15, TimeUnit.SECONDS)
        secondBreak.joinAndTest(15, TimeUnit.SECONDS)

        jvm.waitForExit(2, TimeUnit.SECONDS)

        assertEquals("After", jvm.outputDeque.pollFirst())
        assertEquals(null, jvm.outputDeque.pollFirst())
    }


}