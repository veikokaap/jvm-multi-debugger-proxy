package kaap.veiko.debuggerforker.commands.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.commands.sets.virtualmachine.IdSizesReply;

public class StringId extends ObjectId {
  public StringId(ByteBuffer buffer, IdSizesReply idSizes) {
    super(buffer, idSizes);
  }
}
