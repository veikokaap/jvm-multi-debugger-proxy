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
public abstract class ExceptionEvent extends VirtualMachineEvent {
  public abstract int getRequestId();
  public abstract ThreadId getThread();
  public abstract Location getLocation();
  public abstract TaggedObjectId getException();
  public abstract Location getCatchLocation();

  public EventKind getEventKind() {
    return EventKind.EXCEPTION;
  }

  public static ExceptionEvent create(int requestId, ThreadId thread, Location location, TaggedObjectId exception, Location catchLocation) {
    return new AutoValue_ExceptionEvent(requestId, thread, location, exception, catchLocation);
  }

  public static ExceptionEvent read(DataReader reader) throws DataReadException {
    return create(
        reader.readInt(),
        ThreadId.read(reader),
        Location.read(reader),
        TaggedObjectId.read(reader),
        Location.read(reader)
    );
  }

  @Override
  public void write(DataWriter writer) {
    writer.writeType(getEventKind());
    writer.writeInt(getRequestId());
    writer.writeType(getThread());
    writer.writeType(getLocation());
    writer.writeType(getException());
    writer.writeType(getCatchLocation());
  }
}
