package kaap.veiko.debuggerforker.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.types.IdSizes;

public class ObjectId extends DataTypeBase {
  public ObjectId(PacketDataReader reader) {
    super(reader, IdSizes::getObjectIdSize);
  }
}
