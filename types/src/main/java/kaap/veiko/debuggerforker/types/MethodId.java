package kaap.veiko.debuggerforker.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.types.IdSizes;

public class MethodId extends DataTypeBase {
  public MethodId(PacketDataReader reader) {
    super(reader, IdSizes::getMethodIdSize);
  }
}
