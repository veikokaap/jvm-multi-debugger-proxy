package kaap.veiko.debuggerforker.commands.commandsets.event.events;

import com.google.auto.value.AutoValue;

import kaap.veiko.debuggerforker.types.DataReadException;
import kaap.veiko.debuggerforker.types.DataReader;
import kaap.veiko.debuggerforker.types.DataWriter;
import kaap.veiko.debuggerforker.types.jdwp.EventKind;

@AutoValue
public abstract class ClassUnloadEvent extends VirtualMachineEvent {
  public abstract int getRequestId();
  public abstract String getSignature();

  public EventKind getEventKind() {
    return EventKind.CLASS_UNLOAD;
  }

  public static ClassUnloadEvent create(int requestId, String signature) {
    return new AutoValue_ClassUnloadEvent(requestId, signature);
  }

  public static ClassUnloadEvent read(DataReader reader) throws DataReadException {
    return create(
        reader.readInt(),
        reader.readString()
    );
  }

  @Override
  public void write(DataWriter writer) {
    writer.writeType(getEventKind());
    writer.writeInt(getRequestId());
    writer.writeString(getSignature());
  }
}
