package ee.veikokaap.debugproxy.tests

import java.util.*
import kotlin.test.assertEquals

fun Deque<String>.assertContains(vararg lines: String) {
    lines.forEach{line -> assertEquals(line, this.pollFirst())}
    assertEquals(null, this.pollFirst())
}