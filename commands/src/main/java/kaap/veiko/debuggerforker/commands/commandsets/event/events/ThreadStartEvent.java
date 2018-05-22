package kaap.veiko.debuggerforker.commands.commandsets.event.events;

import com.google.auto.value.AutoValue;

import kaap.veiko.debuggerforker.types.DataReadException;
import kaap.veiko.debuggerforker.types.DataReader;
import kaap.veiko.debuggerforker.types.DataWriter;
import kaap.veiko.debuggerforker.types.jdwp.EventKind;
import kaap.veiko.debuggerforker.types.jdwp.ThreadId;

@AutoValue
public abstract class ThreadStartEvent extends VirtualMachineEvent {
  public abstract int getRequestId();
  public abstract ThreadId getThread();

  public EventKind getEventKind() {
    return EventKind.THREAD_START;
  }

  public static ThreadStartEvent create(int requestId, ThreadId thread) {
    return new AutoValue_ThreadStartEvent(requestId, thread);
  }

  public static ThreadStartEvent read(DataReader reader) throws DataReadException {
    return create(
        reader.readInt(),
        ThreadId.read(reader)
    );
  }

  @Override
  public void write(DataWriter writer) {
    writer.writeType(getEventKind());
    writer.writeInt(getRequestId());
    writer.writeType(getThread());
  }
}
