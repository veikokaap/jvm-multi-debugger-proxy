package kaap.veiko.debuggerforker.types.jdwp;

import kaap.veiko.debuggerforker.types.DataReadException;
import kaap.veiko.debuggerforker.types.DataReader;

public class ClassId extends ReferenceTypeId {
  ClassId(DataReader reader) throws DataReadException {
    super(reader);
  }

  public ClassId(long value) {
    super(value);
  }

  public static ClassId read(DataReader reader) throws DataReadException {
    return new ClassId(reader);
  }
}
