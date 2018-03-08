package ee.veikokaap.debugproxy.tests.singledebugger

import org.junit.Test
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals

open class SingleBreakpointTests : SingleDebuggerTestBase() {

    @Test(timeout = 10000)
    fun `simple break and continue`() = runTest { jvm, debugger ->
        debugger.breakAt(breakpointLocation) {
            assertEquals("Listening for transport dt_socket at address: 16789", jvm.outputDeque.pollFirst())
            assertEquals("Before", jvm.outputDeque.pollFirst())
            assertEquals(null, jvm.outputDeque.pollFirst())
            it.resume()
        }.enable().joinAndTest(2, TimeUnit.SECONDS)

        jvm.waitForExit(2, TimeUnit.SECONDS)

        assertEquals("After", jvm.outputDeque.pollFirst())
        assertEquals(null, jvm.outputDeque.pollFirst())
    }

}
