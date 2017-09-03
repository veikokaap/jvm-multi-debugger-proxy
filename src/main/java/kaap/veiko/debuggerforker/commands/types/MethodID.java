package kaap.veiko.debuggerforker.commands.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.commands.sets.virtualmachine.IDSizesReplyCommand;

public class MethodID extends DataTypeBase {
  public MethodID(ByteBuffer buffer, IDSizesReplyCommand idSizes) {
    super(buffer, idSizes, IDSizesReplyCommand::getMethodIDSize);
  }
}
