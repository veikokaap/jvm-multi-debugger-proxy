package kaap.veiko.debuggerforker.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.types.IdSizes;

public class ClassObjectId extends ObjectId {
  public ClassObjectId(ByteBuffer buffer, IdSizes idSizes) {
    super(buffer, idSizes);
  }
}
