package ee.veikokaap.debugproxy.tests.singledebugger

import ee.veikokaap.debugproxy.tests.assertContainsOnly
import org.junit.Test
import java.util.concurrent.TimeUnit

open class SingleBreakpointTests : SingleDebuggerTestBase() {

    @Test(timeout = 5000)
    fun `simple break and continue`() = runTest { jvm, debugger ->
        val breakpoint = debugger.breakAt(breakpointLocation) { breakpoint ->
            jvm.outputDeque.assertContainsOnly(
                    "Listening for transport dt_socket at address: 16789",
                    "Before")
            breakpoint.resume()
        }

        debugger.allBreakpointSet()

        breakpoint.joinAndTest(1, TimeUnit.SECONDS)
        jvm.waitForExit(1, TimeUnit.SECONDS)

        jvm.outputDeque.assertContainsOnly("After")
    }

}
