package kaap.veiko.debuggerforker.types;

import java.nio.ByteBuffer;

public class TaggedObjectId implements DataType {

  private final byte tag;
  private final ObjectId objectId;

  public TaggedObjectId(PacketDataReader reader) {
    tag = reader.readByte();
    objectId = reader.readType(ObjectId.class);
  }

  public byte getTag() {
    return tag;
  }

  public ObjectId getObjectId() {
    return objectId;
  }

  public long asLong() {
    return getObjectId().asLong();
  }

  @Override
  public void write(PacketDataWriter writer) {
    writer.writeByte(tag);
    writer.writeType(objectId);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    TaggedObjectId that = (TaggedObjectId) o;

    if (tag != that.tag) {
      return false;
    }
    return objectId != null ? objectId.equals(that.objectId) : that.objectId == null;
  }

  @Override
  public int hashCode() {
    int result = (int) tag;
    result = 31 * result + (objectId != null ? objectId.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "TaggedObjectId{" +
        "tag=" + tag +
        ", objectId=" + asLong() +
        '}';
  }
}
