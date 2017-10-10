package kaap.veiko.debuggerforker.commands.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.commands.IdSizes;

public class ThreadId extends ObjectId {
  public ThreadId(ByteBuffer buffer, IdSizes idSizes) {
    super(buffer, idSizes);
  }
}