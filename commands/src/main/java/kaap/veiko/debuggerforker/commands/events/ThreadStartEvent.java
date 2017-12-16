package kaap.veiko.debuggerforker.commands.events;

import kaap.veiko.debuggerforker.types.DataReader;
import kaap.veiko.debuggerforker.types.DataWriter;
import kaap.veiko.debuggerforker.types.jdwp.EventKind;
import kaap.veiko.debuggerforker.types.jdwp.ThreadId;

public class ThreadStartEvent extends VirtualMachineEvent {
  public static final EventKind EVENT_KIND_IDENTIFIER = EventKind.THREAD_START;

  private final int requestId;
  private final ThreadId thread;

  public ThreadStartEvent(DataReader reader) {
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
    return "ThreadStartEvent{" +
        "requestId=" + requestId +
        ", thread=" + thread +
        '}';
  }
}
