package kaap.veiko.debuggerforker.types.jdwp;

import kaap.veiko.debuggerforker.types.DataReader;

public class UntaggedValue extends Value {
  public UntaggedValue(DataReader reader, byte typeTag) {
    super(reader, typeTag);
  }
}
