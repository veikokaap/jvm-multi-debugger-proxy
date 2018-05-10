package ee.veikokaap.debugproxy.tests.singledebugger

import ee.veikokaap.debugproxy.testframework.utils.BreakpointUtil
import ee.veikokaap.debugproxy.tests.StepBreakpointClass
import ee.veikokaap.debugproxy.tests.assertAddedOutput
import ee.veikokaap.debugproxy.tests.runTest
import org.junit.Test

open class StepSingleDebuggerTests {

    val testClass = StepBreakpointClass::class.java
    val firstBreakpoint = BreakpointUtil.findBreakLocation(testClass, 0)
    val secondBreakpoint = BreakpointUtil.findBreakLocation(testClass, 1)

    @Test
    fun `test two breakpoints that step to next line with a single debugger`() =
            runTest(testClass) { jvm, debugger ->
                val firstBreak = debugger.breakAt(firstBreakpoint) {
                    jvm.outputDeque.assertAddedOutput(StepBreakpointClass.BEFORE_MESSAGE)
                } thenStepOver {
                    jvm.outputDeque.assertAddedOutput(StepBreakpointClass.AFTER_BREAKPOINT_0)
                } thenResume {}

                val secondBreak = debugger.breakAt(secondBreakpoint) {
                    jvm.outputDeque.assertAddedOutput(StepBreakpointClass.STEPPED_OVER)
                } thenStepOver {
                    jvm.outputDeque.assertAddedOutput(StepBreakpointClass.AFTER_BREAKPOINT_1)
                } thenResume {}

                debugger.allBreakpointSet()
                firstBreak.joinAndTest()
                secondBreak.joinAndTest()
                jvm.waitForExit()
                jvm.outputDeque.assertAddedOutput(StepBreakpointClass.LAST_MESSAGE)
            }

    @Test
    fun `test stepping onto a existing breakpoint with a single debugger`() = runTest(testClass) { jvm, debugger ->
        val firstBreak = debugger.breakAt(firstBreakpoint) {
            jvm.outputDeque.assertAddedOutput(StepBreakpointClass.BEFORE_MESSAGE)
        } thenStepOver {
            jvm.outputDeque.assertAddedOutput(StepBreakpointClass.AFTER_BREAKPOINT_0)
        } thenStepOver {
            jvm.outputDeque.assertAddedOutput(StepBreakpointClass.STEPPED_OVER, remove = false)
        }

        val secondBreak = debugger.breakAt(secondBreakpoint) {
            jvm.outputDeque.assertAddedOutput(StepBreakpointClass.STEPPED_OVER, remove = false)
            firstBreak.joinAndTest() // wait until first breakpoint and steps have resumed
            jvm.outputDeque.assertAddedOutput(StepBreakpointClass.STEPPED_OVER)
        } thenStepOver {
            jvm.outputDeque.assertAddedOutput(StepBreakpointClass.AFTER_BREAKPOINT_1)
        } thenResume {}

        debugger.allBreakpointSet()

        firstBreak.joinAndTest()
        secondBreak.joinAndTest()
        jvm.waitForExit()

        jvm.outputDeque.assertAddedOutput(StepBreakpointClass.LAST_MESSAGE)
    }


}
