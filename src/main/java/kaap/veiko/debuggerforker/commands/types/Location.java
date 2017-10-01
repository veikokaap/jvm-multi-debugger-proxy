package kaap.veiko.debuggerforker.commands.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.commands.sets.virtualmachine.IdSizesReply;

public class Location {
  private final byte typeTag;
  private final ClassId classId;
  private final MethodId methodId;
  private final long index;

  public Location(ByteBuffer byteBuffer, IdSizesReply idSizes) {
    typeTag = byteBuffer.get();
    classId = new ClassId(byteBuffer, idSizes);
    methodId = new MethodId(byteBuffer, idSizes);
    index = byteBuffer.getLong();
  }

  public byte getTypeTag() {
    return typeTag;
  }

  public ClassId getClassId() {
    return classId;
  }

  public MethodId getMethodId() {
    return methodId;
  }

  public long getIndex() {
    return index;
  }

  @Override
  public String toString() {
    return "Location{" +
        "typeTag=" + typeTag +
        ", classId=" + classId +
        ", methodId=" + methodId +
        ", index=" + index +
        '}';
  }
}
