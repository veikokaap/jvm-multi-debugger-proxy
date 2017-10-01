package kaap.veiko.debuggerforker.commands.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.commands.sets.virtualmachine.IdSizesReply;

public class ObjectId extends DataTypeBase {
  public ObjectId(ByteBuffer buffer, IdSizesReply idSizes) {
    super(buffer, idSizes, IdSizesReply::getObjectIdSize);
  }
}
