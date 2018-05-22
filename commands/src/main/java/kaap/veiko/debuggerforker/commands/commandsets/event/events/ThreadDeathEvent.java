package kaap.veiko.debuggerforker.commands.commandsets.event.events;

import com.google.auto.value.AutoValue;

import kaap.veiko.debuggerforker.types.DataReadException;
import kaap.veiko.debuggerforker.types.DataReader;
import kaap.veiko.debuggerforker.types.DataWriter;
import kaap.veiko.debuggerforker.types.jdwp.EventKind;
import kaap.veiko.debuggerforker.types.jdwp.ThreadId;

@AutoValue
public abstract class ThreadDeathEvent extends VirtualMachineEvent {
  public abstract int getRequestId();
  public abstract ThreadId getThread();

  public EventKind getEventKind() {
    return EventKind.THREAD_DEATH;
  }

  public static ThreadDeathEvent create(int requestId, ThreadId thread) {
    return new AutoValue_ThreadDeathEvent(requestId, thread);
  }

  public static ThreadDeathEvent read(DataReader reader) throws DataReadException {
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
