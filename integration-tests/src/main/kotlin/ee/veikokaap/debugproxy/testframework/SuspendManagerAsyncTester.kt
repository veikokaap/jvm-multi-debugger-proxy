package ee.veikokaap.debugproxy.testframework

import java.util.*
import java.util.function.Consumer

class SuspendManagerAsyncTester(consumer: Consumer<SuspendManager?>): AsyncTester<SuspendManager?>(consumer) {

    private var afterQueue: Deque<AfterBreak> = ArrayDeque();

    override fun accept(t: SuspendManager?) {
        super.accept(t);

        if (exception.get() == null) {
            afterQueue.forEach{
                when (it.afterAction) {
                    AfterAction.RESUME -> t!!.resume()
                    AfterAction.STEP_OVER -> t!!.stepOver(it.listener)
                    AfterAction.STEP_INTO -> t!!.stepInto(it.listener)
                }
            }
        }
    }

    infix fun thenResume(u: () -> Unit): SuspendManagerAsyncTester {
        afterQueue.addLast(AfterBreak(AfterAction.RESUME, null))
        return this
    }

    infix fun thenStepOver(listener: ((SuspendManager) -> Unit)?): SuspendManagerAsyncTester {
        afterQueue.addLast(AfterBreak(AfterAction.STEP_OVER, listener))
        return this
    }

    private data class AfterBreak(val afterAction: AfterAction, val listener: ((SuspendManager) -> Unit)?)

    private enum class AfterAction {
        RESUME, STEP_OVER, STEP_INTO
    }

}
