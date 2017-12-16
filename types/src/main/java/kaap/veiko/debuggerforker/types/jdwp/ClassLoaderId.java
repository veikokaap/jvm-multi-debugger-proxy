package kaap.veiko.debuggerforker.types.jdwp;

import kaap.veiko.debuggerforker.types.DataReader;

public class ClassLoaderId extends ObjectId {
  public static ClassLoaderId read(DataReader reader) {
    return new ClassLoaderId(reader);
  }

  ClassLoaderId(DataReader reader) {
    super(reader);
  }
}
