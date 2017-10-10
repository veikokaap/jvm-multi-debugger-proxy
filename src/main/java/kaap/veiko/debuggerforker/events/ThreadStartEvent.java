package kaap.veiko.debuggerforker.events;

import kaap.veiko.debuggerforker.commands.constants.EventKind;
import kaap.veiko.debuggerforker.commands.parser.annotations.JdwpCommandConstructor;
import kaap.veiko.debuggerforker.commands.types.ThreadId;

@JdwpEvent(EventKind.THREAD_START)
public class ThreadStartEvent extends VirtualMachineEvent {
  private final int requestId;
  private final long thread;

  @JdwpCommandConstructor
  public ThreadStartEvent(
      int requestId,
      ThreadId thread
  ) {
    this.requestId = requestId;
    this.thread = thread.asLong();
  }

  public int getRequestId() {
    return requestId;
  }

  public long getThread() {
    return thread;
  }

  @Override
  public String toString() {
    return "ThreadStartEvent{" +
        "requestId=" + requestId +
        ", thread=" + thread +
        '}';
  }
}
