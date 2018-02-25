package kaap.veiko.debuggerforker.types.jdwp;

import kaap.veiko.debuggerforker.types.DataReader;

public class StringId extends ObjectId {
  public static StringId read(DataReader reader) {
    return new StringId(reader);
  }

  StringId(DataReader reader) {
    super(reader);
  }

  public StringId(long value) {
    super(value);
  }
}
