package kaap.veiko.debuggerforker.commands.commandsets.event.events;

import com.google.auto.value.AutoValue;

import kaap.veiko.debuggerforker.types.DataReadException;
import kaap.veiko.debuggerforker.types.DataReader;
import kaap.veiko.debuggerforker.types.DataWriter;
import kaap.veiko.debuggerforker.types.jdwp.EventKind;
import kaap.veiko.debuggerforker.types.jdwp.Location;
import kaap.veiko.debuggerforker.types.jdwp.ThreadId;

@AutoValue
public abstract class MethodExitEvent extends VirtualMachineEvent {
  public abstract int getRequestId();
  public abstract ThreadId getThread();
  public abstract Location getLocation();

  public EventKind getEventKind() {
    return EventKind.METHOD_EXIT;
  }

  public static MethodExitEvent create(int requestId, ThreadId thread, Location location) {
    return new AutoValue_MethodExitEvent(requestId, thread, location);
  }

  public static MethodExitEvent read(DataReader reader) throws DataReadException {
    return create(
        reader.readInt(),
        ThreadId.read(reader),
        Location.read(reader)
    );
  }

  @Override
  public void write(DataWriter writer) {
    writer.writeType(getEventKind());
    writer.writeInt(getRequestId());
    writer.writeType(getThread());
    writer.writeType(getLocation());
  }
}
