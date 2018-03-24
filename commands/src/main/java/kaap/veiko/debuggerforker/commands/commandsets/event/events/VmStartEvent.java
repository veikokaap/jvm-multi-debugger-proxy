package kaap.veiko.debuggerforker.commands.commandsets.event.events;

import com.google.auto.value.AutoValue;

import kaap.veiko.debuggerforker.types.DataReader;
import kaap.veiko.debuggerforker.types.DataWriter;
import kaap.veiko.debuggerforker.types.jdwp.EventKind;
import kaap.veiko.debuggerforker.types.jdwp.ThreadId;

@AutoValue
public abstract class VmStartEvent extends VirtualMachineEvent {
  public abstract int getRequestId();
  public abstract ThreadId getThread();

  public EventKind getEventKind() {
    return EventKind.VM_START;
  }

  public static VmStartEvent create(int requestId, ThreadId thread) {
    return new AutoValue_VmStartEvent(requestId, thread);
  }

  public static VmStartEvent read(DataReader reader) {
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
