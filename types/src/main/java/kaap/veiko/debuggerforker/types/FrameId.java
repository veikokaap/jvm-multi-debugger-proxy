package kaap.veiko.debuggerforker.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.types.IdSizes;

public class FrameId extends DataTypeBase {
  public FrameId(ByteBuffer buffer, IdSizes idSizes) {
    super(buffer, idSizes, IdSizes::getFrameIdSize);
  }
}
