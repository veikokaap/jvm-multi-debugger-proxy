package kaap.veiko.debuggerforker.types.jdwp;

import kaap.veiko.debuggerforker.types.DataReader;

public class MethodId extends DataTypeBase {
  public MethodId(DataReader reader) {
    super(reader, IdSizes::getMethodIdSize);
  }
}
