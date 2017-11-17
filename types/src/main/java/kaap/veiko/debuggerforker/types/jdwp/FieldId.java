package kaap.veiko.debuggerforker.types.jdwp;

import kaap.veiko.debuggerforker.types.DataReader;

public class FieldId extends DataTypeBase {
  public FieldId(DataReader reader) {
    super(reader, IdSizes::getFieldIdSize);
  }
}
