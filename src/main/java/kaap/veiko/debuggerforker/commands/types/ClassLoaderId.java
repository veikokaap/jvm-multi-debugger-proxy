package kaap.veiko.debuggerforker.commands.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.commands.IdSizes;

public class ClassLoaderId extends ObjectId {
  public ClassLoaderId(ByteBuffer buffer, IdSizes idSizes) {
    super(buffer, idSizes);
  }
}
