package kaap.veiko.debuggerforker.commands.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.commands.IdSizes;

public class ObjectId extends DataTypeBase {
  public ObjectId(ByteBuffer buffer, IdSizes idSizes) {
    super(buffer, idSizes, IdSizes::getObjectIdSize);
  }
}
