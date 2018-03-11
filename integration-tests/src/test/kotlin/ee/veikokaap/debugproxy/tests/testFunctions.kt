package ee.veikokaap.debugproxy.tests

import java.util.*
import kotlin.test.assertEquals

fun Deque<String>.assertContainsOnly(vararg lines: String) {
    lines.forEach{line -> assertEquals(line, this.pollFirst())}
    assertEquals(null, this.pollFirst())
}