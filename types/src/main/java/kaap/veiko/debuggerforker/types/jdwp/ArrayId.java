package kaap.veiko.debuggerforker.types.jdwp;

import kaap.veiko.debuggerforker.types.DataReader;

public class ArrayId extends ObjectId {
  public static ArrayId read(DataReader reader) {
    return new ArrayId(reader);
  }

  ArrayId(DataReader reader) {
    super(reader);
  }
}
