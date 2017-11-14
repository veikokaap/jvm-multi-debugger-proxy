package kaap.veiko.debuggerforker.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.types.IdSizes;

public class ObjectId extends DataTypeBase {
  public ObjectId(ByteBuffer buffer, IdSizes idSizes) {
    super(buffer, idSizes, IdSizes::getObjectIdSize);
  }
}
