package kaap.veiko.debuggerforker.commands.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.commands.sets.virtualmachine.IDSizesReplyCommand;

public class ReferenceTypeID extends ObjectID {
  public ReferenceTypeID(ByteBuffer buffer, IDSizesReplyCommand idSizes) {
    super(buffer, idSizes);
  }
}
