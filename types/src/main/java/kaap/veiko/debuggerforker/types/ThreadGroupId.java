package kaap.veiko.debuggerforker.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.types.IdSizes;

public class ThreadGroupId extends ObjectId {
  public ThreadGroupId(ByteBuffer buffer, IdSizes idSizes) {
    super(buffer, idSizes);
  }
}
