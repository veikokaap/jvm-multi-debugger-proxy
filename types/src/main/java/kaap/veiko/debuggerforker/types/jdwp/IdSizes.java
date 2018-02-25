package kaap.veiko.debuggerforker.types.jdwp;

import kaap.veiko.debuggerforker.types.DataReader;
import kaap.veiko.debuggerforker.types.DataType;
import kaap.veiko.debuggerforker.types.DataWriter;

public class IdSizes implements DataType {
  private final int fieldIdSize;
  private final int methodIdSize;
  private final int objectIdSize;
  private final int referenceTypeIdSize;
  private final int frameIdSize;

  public static IdSizes read(DataReader reader) {
    return new IdSizes(
        reader.readInt(),
        reader.readInt(),
        reader.readInt(),
        reader.readInt(),
        reader.readInt()
    );
  }

  public IdSizes(int fieldIdSize, int methodIdSize, int objectIdSize, int referenceTypeIdSize, int frameIdSize) {
    this.fieldIdSize = fieldIdSize;
    this.methodIdSize = methodIdSize;
    this.objectIdSize = objectIdSize;
    this.referenceTypeIdSize = referenceTypeIdSize;
    this.frameIdSize = frameIdSize;
  }

  @Override
  public void write(DataWriter writer) {
    writer.writeInt(this.getFieldIdSize());
    writer.writeInt(this.getMethodIdSize());
    writer.writeInt(this.getObjectIdSize());
    writer.writeInt(this.getReferenceTypeIdSize());
    writer.writeInt(this.getFrameIdSize());
  }

  public int getSizeOfType(SizeType sizeType) {
    switch (sizeType) {
      case FIELD_ID_SIZE:
        return getFieldIdSize();
      case METHOD_ID_SIZE:
        return getMethodIdSize();
      case OBJECT_ID_SIZE:
        return getObjectIdSize();
      case REFERENCE_TYPE_ID_SIZE:
        return getReferenceTypeIdSize();
      case FRAME_ID_SIZE:
      default:
        return getFrameIdSize();
    }
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

  public enum SizeType {
    FIELD_ID_SIZE,
    METHOD_ID_SIZE,
    OBJECT_ID_SIZE,
    REFERENCE_TYPE_ID_SIZE,
    FRAME_ID_SIZE
  }
}
