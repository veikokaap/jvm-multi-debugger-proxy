package kaap.veiko.debuggerforker.types.jdwp;

import org.checkerframework.checker.nullness.qual.Nullable;

import kaap.veiko.debuggerforker.types.DataReadException;
import kaap.veiko.debuggerforker.types.DataReader;
import kaap.veiko.debuggerforker.types.DataType;
import kaap.veiko.debuggerforker.types.DataWriter;

public class TaggedObjectId implements DataType {

  private final byte tag;
  private final ObjectId objectId;

  TaggedObjectId(DataReader reader) throws DataReadException {
    tag = reader.readByte();
    objectId = ObjectId.read(reader);
  }

  public TaggedObjectId(byte tag, ObjectId objectId) {
    this.tag = tag;
    this.objectId = objectId;
  }

  public static TaggedObjectId read(DataReader reader) throws DataReadException {
    return new TaggedObjectId(reader);
  }

  public byte getTag() {
    return tag;
  }

  public ObjectId getObjectId() {
    return objectId;
  }

  @Override
  public void write(DataWriter writer) {
    writer.writeByte(tag);
    writer.writeType(objectId);
  }

  @Override
  public boolean equals(@Nullable Object o) {
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
        ", objectId=" + getObjectId() +
        '}';
  }
}
