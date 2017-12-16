package kaap.veiko.debuggerforker.types.jdwp;

import kaap.veiko.debuggerforker.types.DataReader;

public class ReferenceTypeId extends ObjectId {
  public static ReferenceTypeId read(DataReader reader) {
    return new ReferenceTypeId(reader);
  }

  ReferenceTypeId(DataReader reader) {
    super(reader);
  }
}
