package kaap.veiko.debuggerforker.commands.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.commands.sets.virtualmachine.IDSizesReplyCommand;

public class StringID extends ObjectID {
  public StringID(ByteBuffer buffer, IDSizesReplyCommand idSizes) {
    super(buffer, idSizes);
  }
}