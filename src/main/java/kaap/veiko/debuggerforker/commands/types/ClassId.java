package kaap.veiko.debuggerforker.commands.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.commands.IdSizes;

public class ClassId extends ReferenceTypeId {
  public ClassId(ByteBuffer buffer, IdSizes idSizes) {
    super(buffer, idSizes);
  }
}
