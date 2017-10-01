package kaap.veiko.debuggerforker.commands.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.commands.sets.virtualmachine.IdSizesReply;

public class FrameId extends DataTypeBase {
  public FrameId(ByteBuffer buffer, IdSizesReply idSizes) {
    super(buffer, idSizes, IdSizesReply::getFrameIdSize);
  }
}
