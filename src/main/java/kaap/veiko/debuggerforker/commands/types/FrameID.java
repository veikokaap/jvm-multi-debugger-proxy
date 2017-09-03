package kaap.veiko.debuggerforker.commands.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.commands.sets.virtualmachine.IDSizesReplyCommand;

public class FrameID extends DataTypeBase {
  public FrameID(ByteBuffer buffer, IDSizesReplyCommand idSizes) {
    super(buffer, idSizes, IDSizesReplyCommand::getFrameIDSize);
  }
}
