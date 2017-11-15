package kaap.veiko.debuggerforker.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.types.IdSizes;

public class FieldId extends DataTypeBase {
  public FieldId(PacketDataReader reader) {
    super(reader, IdSizes::getFieldIdSize);
  }
}
