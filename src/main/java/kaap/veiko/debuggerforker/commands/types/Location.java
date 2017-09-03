package kaap.veiko.debuggerforker.commands.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.commands.sets.virtualmachine.IDSizesReplyCommand;

public class Location {
  private final byte typeTag;
  private final ClassID classID;
  private final MethodID methodID;
  private final long index;

  public Location(ByteBuffer byteBuffer, IDSizesReplyCommand idSizes) {
    typeTag = byteBuffer.get();
    classID = new ClassID(byteBuffer, idSizes);
    methodID = new MethodID(byteBuffer, idSizes);
    index = byteBuffer.getLong();
  }

  public byte getTypeTag() {
    return typeTag;
  }

  public ClassID getClassID() {
    return classID;
  }

  public MethodID getMethodID() {
    return methodID;
  }

  public long getIndex() {
    return index;
  }

  @Override
  public String toString() {
    return "Location{" +
        "typeTag=" + typeTag +
        ", classID=" + classID +
        ", methodID=" + methodID +
        ", index=" + index +
        '}';
  }
}
