package kaap.veiko.debuggerforker.commands.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.commands.sets.virtualmachine.IDSizesReply;

public class FrameID extends DataTypeBase {
  public FrameID(ByteBuffer buffer, IDSizesReply idSizes) {
    super(buffer, idSizes, IDSizesReply::getFrameIDSize);
  }
}
