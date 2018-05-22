package kaap.veiko.debuggerforker.commands.commandsets.event.events;

import com.google.auto.value.AutoValue;

import kaap.veiko.debuggerforker.types.DataReadException;
import kaap.veiko.debuggerforker.types.DataReader;
import kaap.veiko.debuggerforker.types.DataWriter;
import kaap.veiko.debuggerforker.types.jdwp.EventKind;
import kaap.veiko.debuggerforker.types.jdwp.FieldId;
import kaap.veiko.debuggerforker.types.jdwp.Location;
import kaap.veiko.debuggerforker.types.jdwp.ReferenceTypeId;
import kaap.veiko.debuggerforker.types.jdwp.TaggedObjectId;
import kaap.veiko.debuggerforker.types.jdwp.ThreadId;
import kaap.veiko.debuggerforker.types.jdwp.Value;

@AutoValue
public abstract class FieldModificationEvent extends VirtualMachineEvent {
  public abstract int getRequestId();
  public abstract ThreadId getThread();
  public abstract Location getLocation();
  public abstract byte getRefTypeTag();
  public abstract ReferenceTypeId getTypeId();
  public abstract FieldId getFieldId();
  public abstract TaggedObjectId getObjectId();
  public abstract Value getValueToBe();

  public EventKind getEventKind() {
    return EventKind.FIELD_MODIFICATION;
  }

  public static FieldModificationEvent create(int requestId, ThreadId thread, Location location, byte refTypeTag, ReferenceTypeId typeId, FieldId fieldId, TaggedObjectId objectId, Value valueToBe) {
    return new AutoValue_FieldModificationEvent(requestId, thread, location, refTypeTag, typeId, fieldId, objectId, valueToBe);
  }

  public static FieldModificationEvent read(DataReader reader) throws DataReadException {
    return create(
        reader.readInt(),
        ThreadId.read(reader),
        Location.read(reader),
        reader.readByte(),
        ReferenceTypeId.read(reader),
        FieldId.read(reader),
        TaggedObjectId.read(reader),
        Value.read(reader)
    );
  }

  @Override
  public void write(DataWriter writer) {
    writer.writeType(getEventKind());
    writer.writeInt(getRequestId());
    writer.writeType(getThread());
    writer.writeType(getLocation());
    writer.writeByte(getRefTypeTag());
    writer.writeType(getTypeId());
    writer.writeType(getFieldId());
    writer.writeType(getObjectId());
    writer.writeType(getValueToBe());
  }
}