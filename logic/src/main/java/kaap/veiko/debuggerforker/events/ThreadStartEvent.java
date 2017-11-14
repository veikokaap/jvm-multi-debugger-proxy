package kaap.veiko.debuggerforker.events;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.commands.constants.EventKind;
import kaap.veiko.debuggerforker.commands.parser.annotations.JdwpCommandConstructor;
import kaap.veiko.debuggerforker.types.ThreadId;

@JdwpEvent(EventKind.THREAD_START)
public class ThreadStartEvent extends VirtualMachineEvent {
  private final int requestId;
  private final ThreadId thread;

  @JdwpCommandConstructor
  public ThreadStartEvent(
      int requestId,
      ThreadId thread
  ) {
    this.requestId = requestId;
    this.thread = thread;
  }

  public int getRequestId() {
    return requestId;
  }

  public ThreadId getThread() {
    return thread;
  }

  @Override
  public void putToBuffer(ByteBuffer buffer) {
    buffer.putInt(requestId);
    thread.putToBuffer(buffer);
  }

  @Override
  public String toString() {
    return "ThreadStartEvent{" +
        "requestId=" + requestId +
        ", thread=" + thread +
        '}';
  }
}
