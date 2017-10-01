package kaap.veiko.debuggerforker.events;

import kaap.veiko.debuggerforker.commands.constants.EventKind;
import kaap.veiko.debuggerforker.commands.parser.annotations.JdwpCommandConstructor;
import kaap.veiko.debuggerforker.commands.parser.annotations.JdwpSubCommand;
import kaap.veiko.debuggerforker.commands.types.ThreadId;


@JdwpSubCommand(eventKind = EventKind.VM_START)
public class VMStartEvent extends VirtualMachineEvent {
  private final int requestId;
  private final long thread;

  @JdwpCommandConstructor
  public VMStartEvent(
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
    return "VMStartEvent{" +
        "requestId=" + requestId +
        ", thread=" + thread +
        '}';
  }
}
