package kaap.veiko.debuggerforker.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.types.IdSizes;

public class FrameId extends DataTypeBase {
  public FrameId(PacketDataReader reader) {
    super(reader, IdSizes::getFrameIdSize);
  }
}
