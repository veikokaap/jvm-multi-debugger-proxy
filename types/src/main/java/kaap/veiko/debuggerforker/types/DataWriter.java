package kaap.veiko.debuggerforker.types;

public interface DataWriter {
  void writeBoolean(boolean bool);

  void writeByte(byte b);

  void writeShort(short s);

  void writeInt(int i);

  void writeLong(long l);

  void writeLongOfSize(long l, int size);

  void writeString(String string);

  void writeType(DataType type);
}
