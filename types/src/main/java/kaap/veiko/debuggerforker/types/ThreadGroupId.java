package kaap.veiko.debuggerforker.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.types.IdSizes;

public class ThreadGroupId extends ObjectId {
  public ThreadGroupId(PacketDataReader reader) {
    super(reader);
  }
}
