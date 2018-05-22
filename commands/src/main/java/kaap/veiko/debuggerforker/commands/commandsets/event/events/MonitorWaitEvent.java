package kaap.veiko.debuggerforker.commands.commandsets.event.events;

import com.google.auto.value.AutoValue;

import kaap.veiko.debuggerforker.types.DataReadException;
import kaap.veiko.debuggerforker.types.DataReader;
import kaap.veiko.debuggerforker.types.DataWriter;
import kaap.veiko.debuggerforker.types.jdwp.EventKind;
import kaap.veiko.debuggerforker.types.jdwp.Location;
import kaap.veiko.debuggerforker.types.jdwp.TaggedObjectId;
import kaap.veiko.debuggerforker.types.jdwp.ThreadId;

@AutoValue
public abstract class MonitorWaitEvent extends VirtualMachineEvent {
  public abstract int getRequestId();
  public abstract ThreadId getThread();
  public abstract TaggedObjectId getObjectId();
  public abstract Location getLocation();
  public abstract long getTimeout();

  public EventKind getEventKind() {
    return EventKind.MONITOR_WAIT;
  }

  public static MonitorWaitEvent create(int requestId, ThreadId thread, TaggedObjectId objectId, Location location, long timeout) {
    return new AutoValue_MonitorWaitEvent(requestId, thread, objectId, location, timeout);
  }

  public static MonitorWaitEvent read(DataReader reader) throws DataReadException {
    return create(
        reader.readInt(),
        ThreadId.read(reader),
        TaggedObjectId.read(reader),
        Location.read(reader),
        reader.readLong()
    );
  }

  @Override
  public void write(DataWriter writer) {
    writer.writeType(getEventKind());
    writer.writeInt(getRequestId());
    writer.writeType(getThread());
    writer.writeType(getObjectId());
    writer.writeType(getLocation());
    writer.writeLong(getTimeout());
  }
}
