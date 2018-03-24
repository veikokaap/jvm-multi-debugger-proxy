package kaap.veiko.debuggerforker.commands.commandsets.event.events;

import com.google.auto.value.AutoValue;

import kaap.veiko.debuggerforker.types.DataReader;
import kaap.veiko.debuggerforker.types.DataWriter;
import kaap.veiko.debuggerforker.types.jdwp.EventKind;
import kaap.veiko.debuggerforker.types.jdwp.FieldId;
import kaap.veiko.debuggerforker.types.jdwp.Location;
import kaap.veiko.debuggerforker.types.jdwp.ReferenceTypeId;
import kaap.veiko.debuggerforker.types.jdwp.TaggedObjectId;
import kaap.veiko.debuggerforker.types.jdwp.ThreadId;

@AutoValue
public abstract class FieldAccessEvent extends VirtualMachineEvent {
  public abstract int getRequestId();
  public abstract ThreadId getThread();
  public abstract Location getLocation();
  public abstract byte getRefTypeTag();
  public abstract ReferenceTypeId getTypeId();
  public abstract FieldId getFieldId();
  public abstract TaggedObjectId getObjectId();

  public EventKind getEventKind() {
    return EventKind.FIELD_ACCESS;
  }

  public static FieldAccessEvent create(int requestId, ThreadId thread, Location location, byte refTypeTag, ReferenceTypeId typeId, FieldId fieldId, TaggedObjectId objectId) {
    return new AutoValue_FieldAccessEvent(requestId, thread, location, refTypeTag, typeId, fieldId, objectId);
  }

  public static FieldAccessEvent read(DataReader reader) {
    return create(
        reader.readInt(),
        ThreadId.read(reader),
        Location.read(reader),
        reader.readByte(),
        ReferenceTypeId.read(reader),
        FieldId.read(reader),
        TaggedObjectId.read(reader)
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
  }
}
