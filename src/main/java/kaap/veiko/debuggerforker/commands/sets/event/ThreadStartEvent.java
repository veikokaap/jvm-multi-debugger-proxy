package kaap.veiko.debuggerforker.commands.sets.event;

import kaap.veiko.debuggerforker.commands.constants.EventKind;
import kaap.veiko.debuggerforker.commands.parser.annotations.JDWPCommandConstructor;
import kaap.veiko.debuggerforker.commands.parser.annotations.JDWPSubCommand;
import kaap.veiko.debuggerforker.commands.types.ThreadID;

@JDWPSubCommand(eventKind = EventKind.THREAD_START)
public class ThreadStartEvent extends VirtualMachineEvent {
  private final int requestId;
  private final long thread;

  @JDWPCommandConstructor
  public ThreadStartEvent(
      int requestId,
      ThreadID thread
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
