package kaap.veiko.debuggerforker.types.jdwp;

import kaap.veiko.debuggerforker.types.DataReadException;
import kaap.veiko.debuggerforker.types.DataReader;

public class MethodId extends DataTypeBase {
  MethodId(DataReader reader) throws DataReadException {
    super(reader, IdSizes.SizeType.METHOD_ID_SIZE);
  }

  public MethodId(long value) {
    super(value, IdSizes.SizeType.METHOD_ID_SIZE);
  }

  public static MethodId read(DataReader reader) throws DataReadException {
    return new MethodId(reader);
  }
}
