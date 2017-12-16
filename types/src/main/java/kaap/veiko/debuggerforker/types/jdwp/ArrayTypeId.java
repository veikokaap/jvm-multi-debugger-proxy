package kaap.veiko.debuggerforker.types.jdwp;

import kaap.veiko.debuggerforker.types.DataReader;

public class ArrayTypeId extends ReferenceTypeId {
  public static ArrayTypeId read(DataReader reader) {
    return new ArrayTypeId(reader);
  }

  ArrayTypeId(DataReader reader) {
    super(reader);
  }
}
