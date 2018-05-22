package kaap.veiko.debuggerforker.types.jdwp;

import java.util.Arrays;
import org.checkerframework.checker.nullness.qual.Nullable;

import kaap.veiko.debuggerforker.types.DataReadException;
import kaap.veiko.debuggerforker.types.DataReader;
import kaap.veiko.debuggerforker.types.DataType;
import kaap.veiko.debuggerforker.types.DataWriter;

public class ArrayRegion<T> implements DataType {
  private final Type<T> type;
  private final Value[] values;

  ArrayRegion(DataReader reader, byte typeTag) throws DataReadException {
    this(reader, Type.findById(typeTag));
  }

  ArrayRegion(DataReader reader, Type<T> type) throws DataReadException {
    this.type = type;
    int length = reader.readInt();
    values = new Value[length];
    for (int i = 0; i < length; i++) {
      if (type.isPrimitive()) {
        values[i] = UntaggedValue.read(reader, type);
      }
      else {
        values[i] = Value.read(reader);
      }
    }
  }

  public ArrayRegion(Type<T> type, Value<T>[] values) {
    this.type = type;
    this.values = values;
  }

  public static ArrayRegion read(DataReader reader) throws DataReadException {
    byte typeTag = reader.readByte();
    return new ArrayRegion(reader, typeTag);
  }

  public Type getType() {
    return type;
  }

  public Value[] getValue() {
    return values;
  }

  @Override
  public void write(DataWriter writer) {
    writer.writeByte(type.getId());
    writer.writeInt(values.length);
    for (Value value : values) {
      value.write(writer);
    }
  }

  @Override
  public boolean equals(@Nullable Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ArrayRegion<?> that = (ArrayRegion<?>) o;

    if (type != null ? !type.equals(that.type) : that.type != null) {
      return false;
    }

    return Arrays.equals(values, that.values);
  }

  @Override
  public int hashCode() {
    int result = type != null ? type.hashCode() : 0;
    result = 31 * result + Arrays.hashCode(values);
    return result;
  }

  @Override
  public String toString() {
    return "ArrayRegion{" +
        "type=" + type +
        ", values=" + Arrays.toString(values) +
        '}';
  }
}
