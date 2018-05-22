package kaap.veiko.debuggerforker.types.jdwp;

import kaap.veiko.debuggerforker.types.DataReadException;
import kaap.veiko.debuggerforker.types.DataReader;

public class ClassObjectId extends ObjectId {
  ClassObjectId(DataReader reader) throws DataReadException {
    super(reader);
  }

  public ClassObjectId(long value) {
    super(value);
  }

  public static ClassObjectId read(DataReader reader) throws DataReadException {
    return new ClassObjectId(reader);
  }
}
