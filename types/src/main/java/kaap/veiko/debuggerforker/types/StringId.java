package kaap.veiko.debuggerforker.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.types.IdSizes;

public class StringId extends ObjectId {
  public StringId(PacketDataReader reader) {
    super(reader);
  }
}
