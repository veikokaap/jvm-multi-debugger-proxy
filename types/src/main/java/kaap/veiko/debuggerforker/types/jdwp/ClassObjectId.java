package kaap.veiko.debuggerforker.types.jdwp;

import kaap.veiko.debuggerforker.types.DataReader;

public class ClassObjectId extends ObjectId {
  public static ClassObjectId read(DataReader reader) {
    return new ClassObjectId(reader);
  }

  ClassObjectId(DataReader reader) {
    super(reader);
  }

  public ClassObjectId(long value) {
    super(value);
  }
}
