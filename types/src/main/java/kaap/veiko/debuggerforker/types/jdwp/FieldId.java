package kaap.veiko.debuggerforker.types.jdwp;

import kaap.veiko.debuggerforker.types.DataReadException;
import kaap.veiko.debuggerforker.types.DataReader;

public class FieldId extends DataTypeBase {
  FieldId(DataReader reader) throws DataReadException {
    super(reader, IdSizes.SizeType.FIELD_ID_SIZE);
  }

  public FieldId(long value) {
    super(value, IdSizes.SizeType.FIELD_ID_SIZE);
  }

  public static FieldId read(DataReader reader) throws DataReadException {
    return new FieldId(reader);
  }
}
