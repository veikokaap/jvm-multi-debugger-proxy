package kaap.veiko.debuggerforker.types.jdwp;

import java.util.Arrays;

import kaap.veiko.debuggerforker.types.DataType;
import kaap.veiko.debuggerforker.types.DataWriter;

public enum Type implements DataType {
  ARRAY(91, null),
  BYTE(66, 1),
  CHAR(67, 2),
  OBJECT(76, null),
  FLOAT(70, 4),
  DOUBLE(68, 8),
  INT(73, 4),
  LONG(74, 8),
  SHORT(83, 2),
  VOID(86, 0),
  BOOLEAN(90, 1),
  STRING(115, null),
  THREAD(116, null),
  THREAD_GROUP(103, null),
  CLASS_LOADER(108, null),
  CLASS_OBJECT(99, null);

  private final byte id;
  private final Integer size;

  Type(int value, Integer size) {
    this.id = (byte) value;
    this.size = size;
  }

  public static Type findByValue(byte value) {
    return Arrays.stream(Type.values())
        .filter(type -> type.id == value)
        .findFirst().get();
  }

  @Override
  public void write(DataWriter writer) {
    writer.writeByte(id);
  }

  public byte getId() {
    return id;
  }

  public Integer getSize() {
    return size;
  }
}
