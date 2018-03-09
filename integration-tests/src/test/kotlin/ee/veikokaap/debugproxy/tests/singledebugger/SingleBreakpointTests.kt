package ee.veikokaap.debugproxy.tests.singledebugger

import ee.veikokaap.debugproxy.tests.assertContains
import org.junit.Test
import java.util.concurrent.TimeUnit

open class SingleBreakpointTests : SingleDebuggerTestBase() {

    @Test(timeout = 10000)
    fun `simple break and continue`() = runTest { jvm, debugger ->
        debugger.breakAt(breakpointLocation) { breakpoint ->
            jvm.outputDeque.assertContains(
                    "Listening for transport dt_socket at address: 16789",
                    "Before")
            breakpoint.resume()
        }.enable().joinAndTest(2, TimeUnit.SECONDS)

        jvm.waitForExit(2, TimeUnit.SECONDS)
        jvm.outputDeque.assertContains("After")
    }

}
