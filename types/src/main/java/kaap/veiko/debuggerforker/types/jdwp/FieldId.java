package kaap.veiko.debuggerforker.types.jdwp;

import kaap.veiko.debuggerforker.types.DataReader;

public class FieldId extends DataTypeBase {
  public static FieldId read(DataReader reader) {
    return new FieldId(reader);
  }

  FieldId(DataReader reader) {
    super(reader, IdSizes.SizeType.FIELD_ID_SIZE);
  }

  public FieldId(long value) {
    super(value, IdSizes.SizeType.FIELD_ID_SIZE);
  }
}
