package ee.veikokaap.debugproxy.tests.singledebugger

import ee.veikokaap.debugproxy.testframework.utils.BreakpointUtil
import ee.veikokaap.debugproxy.tests.LoopBreakpointClass
import ee.veikokaap.debugproxy.tests.assertAddedOutput
import ee.veikokaap.debugproxy.tests.runTest
import org.junit.Test
import java.util.concurrent.TimeUnit

open class LoopSingleDebuggerTests {

    val testClass = LoopBreakpointClass::class.java
    val breakpoint = BreakpointUtil.findBreakLocation(testClass, 0)

    @Test
    fun `test a single breakpoint in for loop with a single debugger`() = runTest(testClass) { jvm, debugger ->
        var count = 0;
        val breakpoint = debugger.breakAt(breakpoint) { breakpoint ->
            if (count == 0) {
                jvm.outputDeque.assertAddedOutput(LoopBreakpointClass.BEFORE_MESSAGE, LoopBreakpointClass.getBreakpointMessage(0))
            }
            else {
                jvm.outputDeque.assertAddedOutput(LoopBreakpointClass.getBreakpointMessage(count))
            }
            count++;
        } thenResume  {}

        debugger.allBreakpointSet()

        breakpoint.joinAndTest()
        jvm.waitForExit()

        jvm.outputDeque.assertAddedOutput(LoopBreakpointClass.AFTER_MESSAGE)
    }

}
