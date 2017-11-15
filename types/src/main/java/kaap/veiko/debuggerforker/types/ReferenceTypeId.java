package kaap.veiko.debuggerforker.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.types.IdSizes;

public class ReferenceTypeId extends ObjectId {
  public ReferenceTypeId(PacketDataReader reader) {
    super(reader);
  }
}
