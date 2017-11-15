package kaap.veiko.debuggerforker.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.types.IdSizes;

public class ArrayTypeId extends ReferenceTypeId {
  public ArrayTypeId(PacketDataReader reader) {
    super(reader);
  }
}
