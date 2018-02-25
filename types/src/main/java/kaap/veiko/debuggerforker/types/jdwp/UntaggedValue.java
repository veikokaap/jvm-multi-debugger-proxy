package kaap.veiko.debuggerforker.types.jdwp;

import kaap.veiko.debuggerforker.types.DataReader;

public class UntaggedValue extends Value {
  public static UntaggedValue read(DataReader reader, byte typeTag) {
    return new UntaggedValue(reader, typeTag);
  }

  UntaggedValue(DataReader reader, byte typeTag) {
    super(reader, typeTag);
  }

  public UntaggedValue(Type type, Object value) {
    super(type, value);
  }
}
