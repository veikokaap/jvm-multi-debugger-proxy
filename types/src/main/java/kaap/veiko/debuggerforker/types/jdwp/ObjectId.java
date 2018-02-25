package kaap.veiko.debuggerforker.types.jdwp;

import kaap.veiko.debuggerforker.types.DataReader;

public class ObjectId extends DataTypeBase {
  public static ObjectId read(DataReader reader) {
    return new ObjectId(reader);
  }

  ObjectId(DataReader reader) {
    super(reader, IdSizes.SizeType.OBJECT_ID_SIZE);
  }

  public ObjectId(long value) {
    super(value, IdSizes.SizeType.OBJECT_ID_SIZE);
  }
}
