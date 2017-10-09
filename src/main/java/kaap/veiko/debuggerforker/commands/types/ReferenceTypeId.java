package kaap.veiko.debuggerforker.commands.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.commands.IdSizes;

public class ReferenceTypeId extends ObjectId {
  public ReferenceTypeId(ByteBuffer buffer, IdSizes idSizes) {
    super(buffer, idSizes);
  }
}
