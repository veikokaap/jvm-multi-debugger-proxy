package kaap.veiko.debuggerforker.commands.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.commands.sets.virtualmachine.IDSizesReplyCommand;

public class FieldID extends DataTypeBase {
  public FieldID(ByteBuffer buffer, IDSizesReplyCommand idSizes) {
    super(buffer, idSizes, IDSizesReplyCommand::getFieldIDSize);
  }
}
