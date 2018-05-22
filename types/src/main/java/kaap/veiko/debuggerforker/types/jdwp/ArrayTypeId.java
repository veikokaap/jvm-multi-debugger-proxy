package kaap.veiko.debuggerforker.types.jdwp;

import kaap.veiko.debuggerforker.types.DataReadException;
import kaap.veiko.debuggerforker.types.DataReader;

public class ArrayTypeId extends ReferenceTypeId {
  ArrayTypeId(DataReader reader) throws DataReadException {
    super(reader);
  }

  public ArrayTypeId(long value) {
    super(value);
  }

  public static ArrayTypeId read(DataReader reader) throws DataReadException {
    return new ArrayTypeId(reader);
  }
}
