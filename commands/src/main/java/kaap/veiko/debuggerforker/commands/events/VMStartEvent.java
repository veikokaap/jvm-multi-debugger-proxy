package kaap.veiko.debuggerforker.commands.events;

import kaap.veiko.debuggerforker.types.DataReader;
import kaap.veiko.debuggerforker.types.DataWriter;
import kaap.veiko.debuggerforker.types.jdwp.EventKind;
import kaap.veiko.debuggerforker.types.jdwp.ThreadId;

public class VMStartEvent extends VirtualMachineEvent {
  public static final EventKind EVENT_KIND_IDENTIFIER = EventKind.VM_START;

  private final int requestId;
  private final ThreadId thread;

  public VMStartEvent(DataReader reader) {
    this.requestId = reader.readInt();
    this.thread = ThreadId.read(reader);
  }

  @Override
  public void write(DataWriter writer) {
    writer.writeType(EVENT_KIND_IDENTIFIER);
    writer.writeInt(requestId);
    writer.writeType(thread);
  }

  public int getRequestId() {
    return requestId;
  }

  public ThreadId getThread() {
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
