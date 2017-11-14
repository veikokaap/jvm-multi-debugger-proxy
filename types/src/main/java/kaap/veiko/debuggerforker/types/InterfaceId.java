package kaap.veiko.debuggerforker.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.types.IdSizes;

public class InterfaceId extends ReferenceTypeId {
  public InterfaceId(ByteBuffer buffer, IdSizes idSizes) {
    super(buffer, idSizes);
  }
}
