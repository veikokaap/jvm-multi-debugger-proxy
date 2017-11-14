package kaap.veiko.debuggerforker.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.types.IdSizes;

public class ClassId extends ReferenceTypeId {
  public ClassId(ByteBuffer buffer, IdSizes idSizes) {
    super(buffer, idSizes);
  }
}
