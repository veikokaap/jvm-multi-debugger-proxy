package kaap.veiko.debuggerforker.commands.commandsets.event.events;

import com.google.auto.value.AutoValue;

import kaap.veiko.debuggerforker.types.DataReader;
import kaap.veiko.debuggerforker.types.DataWriter;
import kaap.veiko.debuggerforker.types.jdwp.EventKind;
import kaap.veiko.debuggerforker.types.jdwp.Location;
import kaap.veiko.debuggerforker.types.jdwp.TaggedObjectId;
import kaap.veiko.debuggerforker.types.jdwp.ThreadId;

@AutoValue
public abstract class MonitorContendedEnteredEvent extends VirtualMachineEvent {
  public abstract int getRequestId();
  public abstract ThreadId getThread();
  public abstract TaggedObjectId getObjectId();
  public abstract Location getLocation();

  public EventKind getEventKind() {
    return EventKind.MONITOR_CONTENDED_ENTERED;
  }

  public static MonitorContendedEnteredEvent create(int requestId, ThreadId thread, TaggedObjectId objectId, Location location) {
    return new AutoValue_MonitorContendedEnteredEvent(requestId, thread, objectId, location);
  }

  public static MonitorContendedEnteredEvent read(DataReader reader) {
    return create(
        reader.readInt(),
        ThreadId.read(reader),
        TaggedObjectId.read(reader),
        Location.read(reader)
    );
  }

  @Override
  public void write(DataWriter writer) {
    writer.writeType(getEventKind());
    writer.writeInt(getRequestId());
    writer.writeType(getThread());
    writer.writeType(getObjectId());
    writer.writeType(getLocation());
  }
}
