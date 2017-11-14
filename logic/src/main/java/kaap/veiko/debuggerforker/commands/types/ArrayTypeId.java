package kaap.veiko.debuggerforker.commands.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.commands.IdSizes;

public class ArrayTypeId extends ReferenceTypeId {
  public ArrayTypeId(ByteBuffer buffer, IdSizes idSizes) {
    super(buffer, idSizes);
  }
}
