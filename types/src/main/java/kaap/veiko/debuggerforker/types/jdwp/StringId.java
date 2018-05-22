package kaap.veiko.debuggerforker.types.jdwp;

import kaap.veiko.debuggerforker.types.DataReadException;
import kaap.veiko.debuggerforker.types.DataReader;

public class StringId extends ObjectId {
  StringId(DataReader reader) throws DataReadException {
    super(reader);
  }

  public StringId(long value) {
    super(value);
  }

  public static StringId read(DataReader reader) throws DataReadException {
    return new StringId(reader);
  }
}
