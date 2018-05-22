package kaap.veiko.debuggerforker.types.jdwp;

import kaap.veiko.debuggerforker.types.DataReadException;
import kaap.veiko.debuggerforker.types.DataReader;

public class ObjectId extends DataTypeBase {
  ObjectId(DataReader reader) throws DataReadException {
    super(reader, IdSizes.SizeType.OBJECT_ID_SIZE);
  }

  public ObjectId(long value) {
    super(value, IdSizes.SizeType.OBJECT_ID_SIZE);
  }

  public static ObjectId read(DataReader reader) throws DataReadException {
    return new ObjectId(reader);
  }
}
