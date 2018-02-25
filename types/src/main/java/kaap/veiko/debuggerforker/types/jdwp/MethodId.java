package kaap.veiko.debuggerforker.types.jdwp;

import kaap.veiko.debuggerforker.types.DataReader;

public class MethodId extends DataTypeBase {
  public static MethodId read(DataReader reader) {
    return new MethodId(reader);
  }

  MethodId(DataReader reader) {
    super(reader, IdSizes.SizeType.METHOD_ID_SIZE);
  }

  public MethodId(long value) {
    super(value, IdSizes.SizeType.METHOD_ID_SIZE);
  }
}
