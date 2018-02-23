package kaap.veiko.debuggerforker.commands.commandsets.event.events;

import kaap.veiko.debuggerforker.types.DataReader;
import kaap.veiko.debuggerforker.types.DataWriter;
import kaap.veiko.debuggerforker.types.jdwp.EventKind;
import kaap.veiko.debuggerforker.types.jdwp.ReferenceTypeId;
import kaap.veiko.debuggerforker.types.jdwp.ThreadId;

public class ClassPrepareEvent extends VirtualMachineEvent {
  private final int requestId;
  private final ThreadId thread;
  private final byte refTypeTag;
  private final ReferenceTypeId typeId;
  private final String signature;
  private final int status;

  public static ClassPrepareEvent read(DataReader reader) {
    return new ClassPrepareEvent(reader);
  }

  private ClassPrepareEvent(DataReader reader) {
    this.requestId = reader.readInt();
    this.thread = ThreadId.read(reader);
    this.refTypeTag = reader.readByte();
    this.typeId = ReferenceTypeId.read(reader);
    this.signature = reader.readString();
    this.status = reader.readInt();
  }

  @Override
  public void write(DataWriter writer) {
    writer.writeType(EventKind.CLASS_PREPARE);
    writer.writeInt(requestId);
    writer.writeType(thread);
    writer.writeByte(refTypeTag);
    writer.writeType(typeId);
    writer.writeString(signature);
    writer.writeInt(status);
  }

  public int getRequestId() {
    return requestId;
  }

  public ThreadId getThread() {
    return thread;
  }

  public byte getRefTypeTag() {
    return refTypeTag;
  }

  public ReferenceTypeId getTypeId() {
    return typeId;
  }

  public String getSignature() {
    return signature;
  }

  public int getStatus() {
    return status;
  }

  @Override
  public String toString() {
    return "ClassPrepareEvent{" +
        "requestId=" + requestId +
        ", thread=" + thread +
        ", refTypeTag=" + refTypeTag +
        ", typeId=" + typeId +
        ", signature='" + signature + '\'' +
        ", status=" + status +
        '}';
  }
}
