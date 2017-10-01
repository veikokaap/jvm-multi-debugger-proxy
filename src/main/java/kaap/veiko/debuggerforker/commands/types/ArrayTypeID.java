package kaap.veiko.debuggerforker.commands.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.commands.sets.virtualmachine.IDSizesReply;

public class ArrayTypeID extends ReferenceTypeID {
  public ArrayTypeID(ByteBuffer buffer, IDSizesReply idSizes) {
    super(buffer, idSizes);
  }
}
