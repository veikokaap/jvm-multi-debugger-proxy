package kaap.veiko.debuggerforker.commands.commandsets.event.events;

import kaap.veiko.debuggerforker.types.DataReader;
import kaap.veiko.debuggerforker.types.DataWriter;
import kaap.veiko.debuggerforker.types.jdwp.EventKind;
import kaap.veiko.debuggerforker.types.jdwp.ThreadId;

public class ThreadStartEvent extends VirtualMachineEvent {
  private final int requestId;
  private final ThreadId thread;

  public static ThreadStartEvent read(DataReader reader) {
    return new ThreadStartEvent(reader);
  }

  private ThreadStartEvent(DataReader reader) {
    this.requestId = reader.readInt();
    this.thread = ThreadId.read(reader);
  }

  @Override
  public void write(DataWriter writer) {
    writer.writeType(EventKind.THREAD_START);
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
