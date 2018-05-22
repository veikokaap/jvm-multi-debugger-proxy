package kaap.veiko.debuggerforker.types.jdwp;

import kaap.veiko.debuggerforker.types.DataReadException;
import kaap.veiko.debuggerforker.types.DataReader;

public class InterfaceId extends ReferenceTypeId {
  InterfaceId(DataReader reader) throws DataReadException {
    super(reader);
  }

  public InterfaceId(long value) {
    super(value);
  }

  public static InterfaceId read(DataReader reader) throws DataReadException {
    return new InterfaceId(reader);
  }
}
