package kaap.veiko.debuggerforker.commands.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.commands.sets.virtualmachine.IdSizesReply;

public class TaggedObjectId implements DataType {

  private final byte tag;
  private final ObjectId objectId;

  public TaggedObjectId(ByteBuffer byteBuffer, IdSizesReply idSizes) {
    tag = byteBuffer.get();
    objectId = new ObjectId(byteBuffer, idSizes);
  }

  public byte getTag() {
    return tag;
  }

  public ObjectId getObjectId() {
    return objectId;
  }

  @Override
  public long asLong() {
    return getObjectId().asLong();
  }

  @Override
  public String toString() {
    return "TaggedObjectId{" +
        "tag=" + tag +
        ", objectId=" + asLong() +
        '}';
  }
}
