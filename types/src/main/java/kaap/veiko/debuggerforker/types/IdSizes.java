package kaap.veiko.debuggerforker.types;

public class IdSizes implements DataType {
  private final int fieldIdSize;
  private final int methodIdSize;
  private final int objectIdSize;
  private final int referenceTypeIdSize;
  private final int frameIdSize;

  public IdSizes(PacketDataReader reader) {
    this.fieldIdSize = reader.readInt();
    this.methodIdSize = reader.readInt();
    this.objectIdSize = reader.readInt();
    this.referenceTypeIdSize = reader.readInt();
    this.frameIdSize = reader.readInt();
  }

  @Override
  public void write(PacketDataWriter writer) {
    writer.writeInt(this.getFieldIdSize());
    writer.writeInt(this.getMethodIdSize());
    writer.writeInt(this.getObjectIdSize());
    writer.writeInt(this.getReferenceTypeIdSize());
    writer.writeInt(this.getFrameIdSize());
  }

  public int getFieldIdSize() {
    return fieldIdSize;
  }

  public int getMethodIdSize() {
    return methodIdSize;
  }

  public int getObjectIdSize() {
    return objectIdSize;
  }

  public int getReferenceTypeIdSize() {
    return referenceTypeIdSize;
  }

  public int getFrameIdSize() {
    return frameIdSize;
  }

  @Override
  public String toString() {
    return "IdSizes{" +
        "fieldIdSize=" + fieldIdSize +
        ", methodIdSize=" + methodIdSize +
        ", objectIdSize=" + objectIdSize +
        ", referenceTypeIdSize=" + referenceTypeIdSize +
        ", frameIdSize=" + frameIdSize +
        '}';
  }
}
