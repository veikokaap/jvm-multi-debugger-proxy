package kaap.veiko.debuggerforker.commands.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.commands.sets.virtualmachine.IdSizesReply;

public class ArrayId extends ObjectId {
  public ArrayId(ByteBuffer buffer, IdSizesReply idSizes) {
    super(buffer, idSizes);
  }
}
