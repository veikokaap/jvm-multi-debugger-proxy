package kaap.veiko.debuggerforker.types.jdwp;

import kaap.veiko.debuggerforker.types.DataReader;

public class ClassId extends ReferenceTypeId {
  public static ClassId read(DataReader reader) {
    return new ClassId(reader);
  }

  ClassId(DataReader reader) {
    super(reader);
  }

  public ClassId(long value) {
    super(value);
  }
}
