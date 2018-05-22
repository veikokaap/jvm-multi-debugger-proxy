package kaap.veiko.debuggerforker.types.jdwp;

import kaap.veiko.debuggerforker.types.DataReadException;
import kaap.veiko.debuggerforker.types.DataReader;

public class ClassLoaderId extends ObjectId {
  ClassLoaderId(DataReader reader) throws DataReadException {
    super(reader);
  }

  public ClassLoaderId(long value) {
    super(value);
  }

  public static ClassLoaderId read(DataReader reader) throws DataReadException {
    return new ClassLoaderId(reader);
  }
}
