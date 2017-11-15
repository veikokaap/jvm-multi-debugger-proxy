package kaap.veiko.debuggerforker.types;

public class Location implements DataType {
  private final byte typeTag;
  private final ClassId classId;
  private final MethodId methodId;
  private final long index;

  public Location(PacketDataReader reader) {
    typeTag = reader.readByte();
    classId = reader.readType(ClassId.class);
    methodId = reader.readType(MethodId.class);
    index = reader.readLong();
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
  public void write(PacketDataWriter writer) {
    writer.writeByte(typeTag);
    writer.writeType(classId);
    writer.writeType(methodId);
    writer.writeLong(index);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Location location = (Location) o;

    if (typeTag != location.typeTag) {
      return false;
    }
    if (index != location.index) {
      return false;
    }
    if (classId != null ? !classId.equals(location.classId) : location.classId != null) {
      return false;
    }
    return methodId != null ? methodId.equals(location.methodId) : location.methodId == null;
  }

  @Override
  public int hashCode() {
    int result = (int) typeTag;
    result = 31 * result + (classId != null ? classId.hashCode() : 0);
    result = 31 * result + (methodId != null ? methodId.hashCode() : 0);
    result = 31 * result + (int) (index ^ (index >>> 32));
    return result;
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
