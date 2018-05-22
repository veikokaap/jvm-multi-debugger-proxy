package kaap.veiko.debuggerforker.types.jdwp;

import kaap.veiko.debuggerforker.types.DataReadException;
import kaap.veiko.debuggerforker.types.DataReader;

public class ArrayId extends ObjectId {
  ArrayId(DataReader reader) throws DataReadException {
    super(reader);
  }

  public ArrayId(long value) {
    super(value);
  }

  public static ArrayId read(DataReader reader) throws DataReadException {
    return new ArrayId(reader);
  }
}
