package kaap.veiko.debuggerforker.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.types.IdSizes;

public class ReferenceTypeId extends ObjectId {
  public ReferenceTypeId(ByteBuffer buffer, IdSizes idSizes) {
    super(buffer, idSizes);
  }
}
