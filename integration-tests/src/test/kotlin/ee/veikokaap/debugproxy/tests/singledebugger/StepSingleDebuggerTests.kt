package ee.veikokaap.debugproxy.tests.singledebugger

import ee.veikokaap.debugproxy.testframework.utils.BreakpointUtil
import ee.veikokaap.debugproxy.tests.StepBreakpointClass
import ee.veikokaap.debugproxy.tests.assertContainsOnly
import ee.veikokaap.debugproxy.tests.runTest
import org.junit.Test
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals
import kotlin.test.assertTrue

open class StepSingleDebuggerTests {

    val testClass = StepBreakpointClass::class.java
    val firstBreakpoint = BreakpointUtil.findBreakLocation(testClass, 0)
    val secondBreakpoint = BreakpointUtil.findBreakLocation(testClass, 1)

    @Test
    fun `test two breakpoints that step to next line with a single debugger`() = runTest(testClass, 20, TimeUnit.SECONDS) { jvm, debugger ->
        jvm.outputDeque.assertContainsOnly("Listening for transport dt_socket at address: 16789")

        val firstBreak = debugger.breakAt(firstBreakpoint) {
            jvm.outputDeque.assertContainsOnly(StepBreakpointClass.BEFORE_MESSAGE)
        } thenStepOver {
            jvm.outputDeque.assertContainsOnly(StepBreakpointClass.AFTER_BREAKPOINT_0)
        } thenResume {}

        val secondBreak = debugger.breakAt(secondBreakpoint) {
            jvm.outputDeque.assertContainsOnly(StepBreakpointClass.STEPPED_OVER)
        } thenStepOver {
            jvm.outputDeque.assertContainsOnly(StepBreakpointClass.AFTER_BREAKPOINT_1)
        } thenResume {}

        debugger.allBreakpointSet()

        firstBreak.joinAndTest(8, TimeUnit.SECONDS)
        secondBreak.joinAndTest(8, TimeUnit.SECONDS)
        jvm.waitForExit(8, TimeUnit.SECONDS)

        jvm.outputDeque.assertContainsOnly(StepBreakpointClass.LAST_MESSAGE)
    }

    @Test
    fun `test stepping onto a existing breakpoint with a single debugger`() = runTest(testClass, 20, TimeUnit.SECONDS) { jvm, debugger ->
        jvm.outputDeque.assertContainsOnly("Listening for transport dt_socket at address: 16789")

        val firstBreak = debugger.breakAt(firstBreakpoint) {
            jvm.outputDeque.assertContainsOnly(StepBreakpointClass.BEFORE_MESSAGE)
        } thenStepOver {
            jvm.outputDeque.assertContainsOnly(StepBreakpointClass.AFTER_BREAKPOINT_0)
        } thenStepOver {
            assertEquals(StepBreakpointClass.STEPPED_OVER, jvm.outputDeque.peekFirst())
        }

        val secondBreak = debugger.breakAt(secondBreakpoint) {
            assertEquals(StepBreakpointClass.STEPPED_OVER, jvm.outputDeque.peekFirst())
            firstBreak.joinAndTest() // wait until first breakpoint and steps have resumed
            jvm.outputDeque.assertContainsOnly(StepBreakpointClass.STEPPED_OVER)
        } thenStepOver {
            jvm.outputDeque.assertContainsOnly(StepBreakpointClass.AFTER_BREAKPOINT_1)
        } thenResume {}

        debugger.allBreakpointSet()

        firstBreak.joinAndTest(8, TimeUnit.SECONDS)
        secondBreak.joinAndTest(8, TimeUnit.SECONDS)
        jvm.waitForExit(8, TimeUnit.SECONDS)

        jvm.outputDeque.assertContainsOnly(StepBreakpointClass.LAST_MESSAGE)
    }


}
