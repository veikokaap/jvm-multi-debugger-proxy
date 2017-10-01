package kaap.veiko.debuggerforker.commands.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.commands.sets.virtualmachine.IDSizesReply;

public class TaggedObjectID implements DataType {

  private final byte tag;
  private final ObjectID objectID;

  public TaggedObjectID(ByteBuffer byteBuffer, IDSizesReply idSizes) {
    tag = byteBuffer.get();
    objectID = new ObjectID(byteBuffer, idSizes);
  }

  public byte getTag() {
    return tag;
  }

  public ObjectID getObjectID() {
    return objectID;
  }

  @Override
  public long asLong() {
    return getObjectID().asLong();
  }

  @Override
  public String toString() {
    return "TaggedObjectID{" +
        "tag=" + tag +
        ", objectID=" + asLong() +
        '}';
  }
}
