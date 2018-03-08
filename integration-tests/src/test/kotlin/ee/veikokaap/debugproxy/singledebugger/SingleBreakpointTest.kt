package ee.veikokaap.debugproxy.singledebugger

import ee.veikokaap.debugproxy.testframework.DebuggerProcess
import ee.veikokaap.debugproxy.testframework.JvmProcess
import ee.veikokaap.debugproxy.testframework.utils.BreakpointUtil
import org.junit.Test
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals

open class SingleBreakpointTest {

    @Test
    fun `test single breakpoint with a single debugger`() {
        val testClass = SingleBreakpointClass::class.java
        val location = BreakpointUtil.findBreakLocation(testClass, 0)

        val jvm = JvmProcess.runClass(testClass)
        val debugger = DebuggerProcess.attach()

        debugger.createBreakRequestAt(location) {
            assertEquals("Listening for transport dt_socket at address: 16789", jvm.outputDeque.pollFirst())
            assertEquals("Before", jvm.outputDeque.pollFirst())
            assertEquals(null, jvm.outputDeque.pollFirst())
            it.resume()
        }.enable().joinAndTest(5, TimeUnit.SECONDS)

        val exitCode = jvm.waitForExit()

        assertEquals(exitCode, 0)
        assertEquals("After", jvm.outputDeque.pollFirst())
        assertEquals(true, jvm.outputDeque.isEmpty())
    }

    @Test
    fun `test single breakpoint with 2 debuggers`() {
        val testClass = SingleBreakpointClass::class.java
        val location = BreakpointUtil.findBreakLocation(testClass, 0)

        val jvm = JvmProcess.runClass(testClass)
        val debugger = DebuggerProcess.attach()

        val firstBreak = debugger.createBreakRequestAt(location) {
            assertEquals("Listening for transport dt_socket at address: 16789", jvm.outputDeque.pollFirst())
            assertEquals("Before", jvm.outputDeque.pollFirst())
            assertEquals(null, jvm.outputDeque.pollFirst())
            it.resume()
        }

        val secondBreak = debugger.createBreakRequestAt(location) {
            firstBreak.joinAndTest(2, TimeUnit.SECONDS)
            assertEquals(null, jvm.outputDeque.pollFirst())
            it.resume()
        }

        firstBreak.enable()
        secondBreak.enable()
        secondBreak.joinAndTest(5, TimeUnit.SECONDS)

        val exitCode = jvm.waitForExit()

        assertEquals(exitCode, 0)
        assertEquals("After", jvm.outputDeque.pollFirst())
        assertEquals(null, jvm.outputDeque.pollFirst())
    }

    @Test
    fun `test 2 breakpoints with 2 debuggers`() {
        val testClass = DoubleBreakpointClass::class.java
        val firstLocation = BreakpointUtil.findBreakLocation(testClass, 0)
        val secondLocation = BreakpointUtil.findBreakLocation(testClass, 1)

        val jvm = JvmProcess.runClass(testClass)
        val firstDebugger = DebuggerProcess.attach()
        val secondDebugger = DebuggerProcess.attach()

        val firstBreak = firstDebugger.createBreakRequestAt(firstLocation) {
            assertEquals("Listening for transport dt_socket at address: 16789", jvm.outputDeque.pollFirst())
            assertEquals("Before", jvm.outputDeque.pollFirst())
            assertEquals(null, jvm.outputDeque.pollFirst())
            it.resume()
        }

        val secondBreak = secondDebugger.createBreakRequestAt(secondLocation) {
            assertEquals("Between", jvm.outputDeque.pollFirst())
            assertEquals(null, jvm.outputDeque.pollFirst())
            it.resume()
        }

        firstBreak.enable()
        secondBreak.enable()
        firstBreak.joinAndTest(15, TimeUnit.SECONDS)
        secondBreak.joinAndTest(15, TimeUnit.SECONDS)

        val exitCode = jvm.waitForExit()

        assertEquals(exitCode, 0)
        assertEquals("After", jvm.outputDeque.pollFirst())
        assertEquals(null, jvm.outputDeque.pollFirst())
    }
}
