package kaap.veiko.debuggerforker.commands.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.commands.IdSizes;

public class FrameId extends DataTypeBase {
  public FrameId(ByteBuffer buffer, IdSizes idSizes) {
    super(buffer, idSizes, IdSizes::getFrameIdSize);
  }
}
