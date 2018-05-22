package kaap.veiko.debuggerforker.types.jdwp;

import kaap.veiko.debuggerforker.types.DataReadException;
import kaap.veiko.debuggerforker.types.DataReader;

public class ReferenceTypeId extends ObjectId {
  ReferenceTypeId(DataReader reader) throws DataReadException {
    super(reader);
  }

  public ReferenceTypeId(long value) {
    super(value);
  }

  public static ReferenceTypeId read(DataReader reader) throws DataReadException {
    return new ReferenceTypeId(reader);
  }
}
