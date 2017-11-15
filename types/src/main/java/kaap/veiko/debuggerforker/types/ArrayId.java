package kaap.veiko.debuggerforker.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.types.IdSizes;

public class ArrayId extends ObjectId {
  public ArrayId(PacketDataReader reader) {
    super(reader);
  }
}
