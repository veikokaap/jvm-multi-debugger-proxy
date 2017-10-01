package kaap.veiko.debuggerforker.commands.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.commands.sets.virtualmachine.IdSizesReply;

public class ThreadGroupId extends ObjectId {
  public ThreadGroupId(ByteBuffer buffer, IdSizesReply idSizes) {
    super(buffer, idSizes);
  }
}
