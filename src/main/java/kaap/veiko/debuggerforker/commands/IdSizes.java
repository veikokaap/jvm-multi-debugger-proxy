package kaap.veiko.debuggerforker.commands;

public class IdSizes {
  private final int fieldIdSize;
  private final int methodIdSize;
  private final int objectIdSize;
  private final int referenceTypeIdSize;
  private final int frameIdSize;

  public IdSizes(int fieldIdSize, int methodIdSize, int objectIdSize, int referenceTypeIdSize, int frameIdSize) {
    this.fieldIdSize = fieldIdSize;
    this.methodIdSize = methodIdSize;
    this.objectIdSize = objectIdSize;
    this.referenceTypeIdSize = referenceTypeIdSize;
    this.frameIdSize = frameIdSize;
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
