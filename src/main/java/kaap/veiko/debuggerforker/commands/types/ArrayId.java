package kaap.veiko.debuggerforker.commands.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.commands.IdSizes;

public class ArrayId extends ObjectId {
  public ArrayId(ByteBuffer buffer, IdSizes idSizes) {
    super(buffer, idSizes);
  }
}
