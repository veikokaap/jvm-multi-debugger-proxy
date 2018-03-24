package kaap.veiko.debuggerforker.commands.commandsets.event.events;

import com.google.auto.value.AutoValue;

import kaap.veiko.debuggerforker.types.DataReader;
import kaap.veiko.debuggerforker.types.DataWriter;
import kaap.veiko.debuggerforker.types.jdwp.EventKind;

@AutoValue
public abstract class VmDeathEvent extends VirtualMachineEvent {
  public abstract int getRequestId();

  public EventKind getEventKind() {
    return EventKind.VM_DEATH;
  }

  public static VmDeathEvent create(int requestId) {
    return new AutoValue_VmDeathEvent(requestId);
  }

  public static VmDeathEvent read(DataReader reader) {
    return create(reader.readInt());
  }

  @Override
  public void write(DataWriter writer) {
    writer.writeType(getEventKind());
    writer.writeInt(getRequestId());
  }
}
