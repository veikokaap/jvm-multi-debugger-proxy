package kaap.veiko.debuggerforker.types.jdwp;

import kaap.veiko.debuggerforker.types.DataReader;
import kaap.veiko.debuggerforker.types.DataType;
import kaap.veiko.debuggerforker.types.DataWriter;

public class Value implements DataType {
  private final Type type;
  private final Object value;

  public static Value read(DataReader reader) {
    return new Value(reader);
  }

  private Value(DataReader reader) {
    this(reader, reader.readByte());
  }

  protected Value(DataReader reader, byte typeTag) {
    this.type = Type.findByValue(typeTag);
    value = type.read(reader);
  }

  public Value(Type type, Object value) {
    this.type = type;
    this.value = value;
  }

  public Type getType() {
    return type;
  }

  public Object getValue() {
    return value;
  }

  @Override
  public void write(DataWriter writer) {
    writer.writeByte(type.getId());
    type.write(writer, value);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Value value1 = (Value) o;

    if (type != value1.type) {
      return false;
    }
    return value != null ? value.equals(value1.value) : value1.value == null;
  }

  @Override
  public int hashCode() {
    int result = type != null ? type.hashCode() : 0;
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
