package kaap.veiko.debuggerforker.commands.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.commands.IdSizes;

public class ThreadGroupId extends ObjectId {
  public ThreadGroupId(ByteBuffer buffer, IdSizes idSizes) {
    super(buffer, idSizes);
  }
}
