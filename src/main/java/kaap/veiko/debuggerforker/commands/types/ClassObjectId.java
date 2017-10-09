package kaap.veiko.debuggerforker.commands.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.commands.IdSizes;

public class ClassObjectId extends ObjectId {
  public ClassObjectId(ByteBuffer buffer, IdSizes idSizes) {
    super(buffer, idSizes);
  }
}
