package kaap.veiko.debuggerforker.commands.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.commands.sets.virtualmachine.IDSizesReplyCommand;

public class ClassObjectID extends ObjectID {
  public ClassObjectID(ByteBuffer buffer, IDSizesReplyCommand idSizes) {
    super(buffer, idSizes);
  }
}
