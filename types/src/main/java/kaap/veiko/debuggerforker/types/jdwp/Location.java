package kaap.veiko.debuggerforker.types.jdwp;

import org.checkerframework.checker.nullness.qual.Nullable;

import kaap.veiko.debuggerforker.types.DataReader;
import kaap.veiko.debuggerforker.types.DataType;
import kaap.veiko.debuggerforker.types.DataWriter;

public class Location implements DataType {
  private final byte typeTag;
  private final ClassId classId;
  private final MethodId methodId;
  private final long index;

  public static Location read(DataReader reader) {
    return new Location(reader);
  }

  Location(DataReader reader) {
    typeTag = reader.readByte();
    classId = ClassId.read(reader);
    methodId = MethodId.read(reader);
    index = reader.readLong();
  }

  public Location(byte typeTag, ClassId classId, MethodId methodId, long index) {
    this.typeTag = typeTag;
    this.classId = classId;
    this.methodId = methodId;
    this.index = index;
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
  public void write(DataWriter writer) {
    writer.writeByte(typeTag);
    writer.writeType(classId);
    writer.writeType(methodId);
    writer.writeLong(index);
  }

  @Override
  public boolean equals(@Nullable Object o) {
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
