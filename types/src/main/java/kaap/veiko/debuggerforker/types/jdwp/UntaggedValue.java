package kaap.veiko.debuggerforker.types.jdwp;

import kaap.veiko.debuggerforker.types.DataReadException;
import kaap.veiko.debuggerforker.types.DataReader;

public class UntaggedValue extends Value {
  UntaggedValue(DataReader reader, byte typeTag) throws DataReadException {
    super(reader, typeTag);
  }

  public UntaggedValue(Type type, Object value) {
    super(type, value);
  }

  public static UntaggedValue read(DataReader reader, byte typeTag) throws DataReadException {
    return new UntaggedValue(reader, typeTag);
  }
}
