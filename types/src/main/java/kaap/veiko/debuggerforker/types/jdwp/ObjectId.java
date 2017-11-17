package kaap.veiko.debuggerforker.types.jdwp;

import kaap.veiko.debuggerforker.types.DataReader;

public class ObjectId extends DataTypeBase {
  public ObjectId(DataReader reader) {
    super(reader, IdSizes::getObjectIdSize);
  }
}
