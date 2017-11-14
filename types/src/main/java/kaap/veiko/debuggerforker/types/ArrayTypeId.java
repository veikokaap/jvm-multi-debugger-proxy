package kaap.veiko.debuggerforker.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.types.IdSizes;

public class ArrayTypeId extends ReferenceTypeId {
  public ArrayTypeId(ByteBuffer buffer, IdSizes idSizes) {
    super(buffer, idSizes);
  }
}
