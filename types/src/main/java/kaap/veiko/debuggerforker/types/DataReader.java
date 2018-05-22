package kaap.veiko.debuggerforker.types;

import kaap.veiko.debuggerforker.types.jdwp.IdSizes;

public interface DataReader {
  byte readByte() throws DataReadException;

  short readShort() throws DataReadException;

  int readInt() throws DataReadException;

  long readLong() throws DataReadException;

  long readLongOfSize(IdSizes.SizeType sizeType) throws DataReadException;

  boolean readBoolean() throws DataReadException;

  float readFloat() throws DataReadException;

  double readDouble() throws DataReadException;

  String readString() throws DataReadException;
}
