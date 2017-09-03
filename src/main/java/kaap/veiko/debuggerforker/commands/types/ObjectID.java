package kaap.veiko.debuggerforker.commands.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.commands.sets.virtualmachine.IDSizesReplyCommand;

public class ObjectID extends DataTypeBase {
  public ObjectID(ByteBuffer buffer, IDSizesReplyCommand idSizes) {
    super(buffer, idSizes, IDSizesReplyCommand::getObjectIDSize);
  }
}
