package kaap.veiko.debuggerforker.commands.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.commands.sets.virtualmachine.IDSizesReply;

public class StringID extends ObjectID {
  public StringID(ByteBuffer buffer, IDSizesReply idSizes) {
    super(buffer, idSizes);
  }
}
