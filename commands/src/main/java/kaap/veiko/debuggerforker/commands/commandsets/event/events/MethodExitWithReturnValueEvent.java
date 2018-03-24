package kaap.veiko.debuggerforker.commands.commandsets.event.events;

import com.google.auto.value.AutoValue;

import kaap.veiko.debuggerforker.types.DataReader;
import kaap.veiko.debuggerforker.types.DataWriter;
import kaap.veiko.debuggerforker.types.jdwp.EventKind;
import kaap.veiko.debuggerforker.types.jdwp.Location;
import kaap.veiko.debuggerforker.types.jdwp.ThreadId;
import kaap.veiko.debuggerforker.types.jdwp.Value;

@AutoValue
public abstract class MethodExitWithReturnValueEvent extends VirtualMachineEvent {
  public abstract int getRequestId();
  public abstract ThreadId getThread();
  public abstract Location getLocation();
  public abstract Value getValue();

  public EventKind getEventKind() {
    return EventKind.METHOD_EXIT_WITH_RETURN_VALUE;
  }

  public static MethodExitWithReturnValueEvent create(int requestId, ThreadId thread, Location location, Value value) {
    return new AutoValue_MethodExitWithReturnValueEvent(requestId, thread, location, value);
  }

  public static MethodExitWithReturnValueEvent read(DataReader reader) {
    return create(
        reader.readInt(),
        ThreadId.read(reader),
        Location.read(reader),
        Value.read(reader)
    );
  }

  @Override
  public void write(DataWriter writer) {
    writer.writeType(getEventKind());
    writer.writeInt(getRequestId());
    writer.writeType(getThread());
    writer.writeType(getLocation());
    writer.writeType(getValue());
  }
}
