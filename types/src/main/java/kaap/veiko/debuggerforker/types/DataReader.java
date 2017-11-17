package kaap.veiko.debuggerforker.types;

import kaap.veiko.debuggerforker.types.jdwp.IdSizes;

public interface DataReader {
  byte readByte();

  short readShort();

  int readInt();

  long readLong();

  long readLongOfSize(int size);

  boolean readBoolean();

  String readString();

  IdSizes getIdSizes();

  @SuppressWarnings("unchecked")
  <T extends DataType> T readType(Class<T> clazz);
}
