package kaap.veiko.debuggerforker.commands.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.commands.IdSizes;

public class InterfaceId extends ReferenceTypeId {
  public InterfaceId(ByteBuffer buffer, IdSizes idSizes) {
    super(buffer, idSizes);
  }
}
