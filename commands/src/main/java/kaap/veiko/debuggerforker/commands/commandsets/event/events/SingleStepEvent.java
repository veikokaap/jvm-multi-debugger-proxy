package kaap.veiko.debuggerforker.commands.commandsets.event.events;

import com.google.auto.value.AutoValue;

import kaap.veiko.debuggerforker.types.DataReader;
import kaap.veiko.debuggerforker.types.DataWriter;
import kaap.veiko.debuggerforker.types.jdwp.EventKind;
import kaap.veiko.debuggerforker.types.jdwp.Location;
import kaap.veiko.debuggerforker.types.jdwp.ThreadId;

@AutoValue
public abstract class SingleStepEvent extends VirtualMachineEvent {
  public abstract int getRequestId();
  public abstract ThreadId getThread();
  public abstract Location getLocation();

  public EventKind getEventKind() {
    return EventKind.SINGLE_STEP;
  }

  public static SingleStepEvent create(int requestId, ThreadId thread, Location location) {
    return new AutoValue_SingleStepEvent(requestId, thread, location);
  }

  public static SingleStepEvent read(DataReader reader) {
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
