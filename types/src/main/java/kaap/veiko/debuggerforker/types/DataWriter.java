package kaap.veiko.debuggerforker.types;

import kaap.veiko.debuggerforker.types.jdwp.IdSizes;

public interface DataWriter {
  void writeBoolean(boolean bool);

  void writeByte(byte b);

  void writeShort(short s);

  void writeInt(int i);

  void writeLong(long l);

  void writeLongOfSize(long l, IdSizes.SizeType sizeType);

  void writeString(String string);

  void writeType(DataType type);
}
