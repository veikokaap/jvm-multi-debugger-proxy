package kaap.veiko.debuggerforker.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.types.IdSizes;

public class ThreadId extends ObjectId {
  public ThreadId(ByteBuffer buffer, IdSizes idSizes) {
    super(buffer, idSizes);
  }
}
