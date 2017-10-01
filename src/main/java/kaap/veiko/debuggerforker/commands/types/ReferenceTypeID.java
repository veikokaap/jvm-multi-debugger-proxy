package kaap.veiko.debuggerforker.commands.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.commands.sets.virtualmachine.IDSizesReply;

public class ReferenceTypeID extends ObjectID {
  public ReferenceTypeID(ByteBuffer buffer, IDSizesReply idSizes) {
    super(buffer, idSizes);
  }
}
