package kaap.veiko.debuggerforker.types.jdwp;

import org.checkerframework.checker.nullness.qual.Nullable;

import kaap.veiko.debuggerforker.types.DataReadException;
import kaap.veiko.debuggerforker.types.DataReader;
import kaap.veiko.debuggerforker.types.DataType;
import kaap.veiko.debuggerforker.types.DataWriter;

public class Value<T> implements DataType {
  private final Type<T> type;
  private final @Nullable T value;

  Value(DataReader reader, byte typeTag) throws DataReadException {
    this(reader, Type.findById(typeTag));
  }

  Value(DataReader reader, Type<T> type) throws DataReadException {
    this.type = type;
    this.value = type.read(reader);
  }

  public Value(Type<T> type, T value) {
    this.type = type;
    this.value = value;
  }

  public static Value read(DataReader reader) throws DataReadException {
    byte typeTag = reader.readByte();
    return new Value(reader, typeTag);
  }

  public Type<T> getType() {
    return type;
  }

  public @Nullable T getValue() {
    return value;
  }

  @Override
  public void write(DataWriter writer) {
    writer.writeByte(type.getId());
    if (value != null) {
      getType().write(writer, value);
    }
    else if (getType() != Type.VOID) {
      throw new NullPointerException("Value contains null for non-void type.");
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

    Value value1 = (Value) o;

    if (!type.equals(value1.type)) {
      return false;
    }
    return value != null && value.equals(value1.value) || value == null && value1.value == null;
  }

  @Override
  public int hashCode() {
    int result = type.hashCode();
    result = 31 * result + (value != null ? value.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Value{" +
        "type=" + type +
        ", value=" + value +
        '}';
  }
}
