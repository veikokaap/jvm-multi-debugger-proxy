package kaap.veiko.debuggerforker.types;

import kaap.veiko.debuggerforker.types.jdwp.IdSizes;

public interface DataReader {
  byte readByte();

  short readShort();

  int readInt();

  long readLong();

  long readLongOfSize(IdSizes.SizeType sizeType);

  boolean readBoolean();

  float readFloat();

  double readDouble();

  String readString();
}
