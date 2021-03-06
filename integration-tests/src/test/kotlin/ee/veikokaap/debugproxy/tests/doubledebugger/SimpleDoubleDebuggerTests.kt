package ee.veikokaap.debugproxy.tests.doubledebugger

import ee.veikokaap.debugproxy.testframework.utils.BreakpointUtil
import ee.veikokaap.debugproxy.tests.SimpleBreakpointClass
import ee.veikokaap.debugproxy.tests.assertAddedOutput
import ee.veikokaap.debugproxy.tests.runTest
import org.junit.Test
import java.util.concurrent.TimeUnit
import kotlin.test.assertTrue

open class SimpleDoubleDebuggerTests {

    val testClass = SimpleBreakpointClass::class.java
    val firstLocation = BreakpointUtil.findBreakLocation(testClass, 0)
    val secondLocation = BreakpointUtil.findBreakLocation(testClass, 1)

    @Test
    fun `test single breakpoint with 2 debuggers`() = runTest(testClass) { jvm, firstDebugger, secondDebugger ->
        val firstBreak = firstDebugger.breakAt(firstLocation) {
            jvm.outputDeque.assertAddedOutput("Before breakpoints")
        } thenResume {}

        val secondBreak = secondDebugger.breakAt(firstLocation) {
            firstBreak.joinAndTest(4, TimeUnit.SECONDS)
            assertTrue(jvm.outputDeque.isEmpty())
        } thenResume {}

        firstDebugger.allBreakpointSet()

        secondBreak.joinAndTest()
        jvm.waitForExit()

        jvm.outputDeque.assertAddedOutput("After breakpoint 0", "After breakpoint 1")
    }

    @Test
    fun `test 2 breakpoints with 2 debuggers`() = runTest(testClass) { jvm, firstDebugger, secondDebugger ->
        val firstBreak = firstDebugger.breakAt(firstLocation) {
            jvm.outputDeque.assertAddedOutput("Before breakpoints")
        } thenResume {}

        val secondBreak = secondDebugger.breakAt(secondLocation) {
            jvm.outputDeque.assertAddedOutput("After breakpoint 0")
        } thenResume {}

        firstDebugger.allBreakpointSet()
        secondDebugger.allBreakpointSet()

        firstBreak.joinAndTest()
        secondBreak.joinAndTest()
        jvm.waitForExit()

        jvm.outputDeque.assertAddedOutput("After breakpoint 1")
    }


}