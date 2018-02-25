package kaap.veiko.debuggerforker.types.jdwp;

import kaap.veiko.debuggerforker.types.DataReader;

public class InterfaceId extends ReferenceTypeId {
  public static InterfaceId read(DataReader reader) {
    return new InterfaceId(reader);
  }

  InterfaceId(DataReader reader) {
    super(reader);
  }

  public InterfaceId(long value) {
    super(value);
  }
}
