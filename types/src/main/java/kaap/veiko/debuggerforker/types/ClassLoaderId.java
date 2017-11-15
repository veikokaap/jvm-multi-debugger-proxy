package kaap.veiko.debuggerforker.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.types.IdSizes;

public class ClassLoaderId extends ObjectId {
  public ClassLoaderId(PacketDataReader reader) {
    super(reader);
  }
}
