package kaap.veiko.debuggerforker.commands.commandsets.event.events;

import com.google.auto.value.AutoValue;

import kaap.veiko.debuggerforker.types.DataReadException;
import kaap.veiko.debuggerforker.types.DataReader;
import kaap.veiko.debuggerforker.types.DataWriter;
import kaap.veiko.debuggerforker.types.jdwp.EventKind;
import kaap.veiko.debuggerforker.types.jdwp.ReferenceTypeId;
import kaap.veiko.debuggerforker.types.jdwp.ThreadId;

@AutoValue
public abstract class ClassPrepareEvent extends VirtualMachineEvent {
  public abstract int getRequestId();
  public abstract ThreadId getThread();
  public abstract byte getRefTypeTag();
  public abstract ReferenceTypeId getTypeId();
  public abstract String getSignature();
  public abstract int getStatus();

  public EventKind getEventKind() {
    return EventKind.CLASS_PREPARE;
  }

  public static ClassPrepareEvent create(int requestId, ThreadId thread, byte refTypeTag, ReferenceTypeId typeId, String signature, int status) {
    return new AutoValue_ClassPrepareEvent(requestId, thread, refTypeTag, typeId, signature, status);
  }

  public static ClassPrepareEvent read(DataReader reader) throws DataReadException {
    return create(
        reader.readInt(),
        ThreadId.read(reader),
        reader.readByte(),
        ReferenceTypeId.read(reader),
        reader.readString(),
        reader.readInt()
    );
  }

  @Override
  public void write(DataWriter writer) {
    writer.writeType(getEventKind());
    writer.writeInt(getRequestId());
    writer.writeType(getThread());
    writer.writeByte(getRefTypeTag());
    writer.writeType(getTypeId());
    writer.writeString(getSignature());
    writer.writeInt(getStatus());
  }
}
