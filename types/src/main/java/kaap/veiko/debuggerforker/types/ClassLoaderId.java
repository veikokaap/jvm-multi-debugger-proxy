package kaap.veiko.debuggerforker.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.types.IdSizes;

public class ClassLoaderId extends ObjectId {
  public ClassLoaderId(ByteBuffer buffer, IdSizes idSizes) {
    super(buffer, idSizes);
  }
}
