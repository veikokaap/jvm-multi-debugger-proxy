package kaap.veiko.debuggerforker.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.utils.ByteBufferUtil;

public class Value implements DataType {
  private final Type type;
  private final Long value;

  public Value(ByteBuffer byteBuffer, IdSizes idSizes) {
    this(byteBuffer, idSizes, byteBuffer.get());
  }

  protected Value(ByteBuffer byteBuffer, IdSizes idSizes, byte typeTag) {
    this.type = Type.findByValue(typeTag);
    Integer size = type.getSize();

    if (size == null) {
      value = ByteBufferUtil.getLong(byteBuffer, idSizes.getObjectIdSize());
    }
    else if (size == 0) {
      value = null;
    }
    else {
      value = ByteBufferUtil.getLong(byteBuffer, size);
    }
  }

  public Type getType() {
    return type;
  }

  public Long getValue() {
    return value;
  }

  @Override
  public void putToBuffer(ByteBuffer buffer) {
    buffer.put(type.getId());
    buffer.putLong(value);
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
