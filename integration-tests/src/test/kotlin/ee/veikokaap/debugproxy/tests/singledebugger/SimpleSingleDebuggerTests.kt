package ee.veikokaap.debugproxy.tests.singledebugger

import ee.veikokaap.debugproxy.testframework.utils.BreakpointUtil
import ee.veikokaap.debugproxy.tests.SimpleBreakpointClass
import ee.veikokaap.debugproxy.tests.assertAddedOutput
import ee.veikokaap.debugproxy.tests.runTest
import org.junit.Test

open class SimpleSingleDebuggerTests {

    val testClass = SimpleBreakpointClass::class.java
    val firstBreakpoint = BreakpointUtil.findBreakLocation(testClass, 0)
    val secondBreakpoint = BreakpointUtil.findBreakLocation(testClass, 1)

    @Test
    fun `test a single breakpoint with a single debugger`() = runTest(testClass) { jvm, debugger ->
        val breakpoint = debugger.breakAt(firstBreakpoint) { breakpoint ->
            jvm.outputDeque.assertAddedOutput("Before breakpoints")
        } thenResume {}

        debugger.allBreakpointSet()

        breakpoint.joinAndTest()
        jvm.waitForExit()

        jvm.outputDeque.assertAddedOutput("After breakpoint 0", "After breakpoint 1")
    }

    @Test
    fun `test 2 breakpoints with a single debugger`() = runTest(testClass) { jvm, debugger ->
        val firstBreak = debugger.breakAt(firstBreakpoint) {
            jvm.outputDeque.assertAddedOutput("Before breakpoints")
        } thenResume {}

        val secondBreak = debugger.breakAt(secondBreakpoint) {
            jvm.outputDeque.assertAddedOutput("After breakpoint 0")
        } thenResume {}

        debugger.allBreakpointSet()

        firstBreak.joinAndTest()
        secondBreak.joinAndTest()
        jvm.waitForExit()

        jvm.outputDeque.assertAddedOutput("After breakpoint 1")
    }

}
