package kaap.veiko.debuggerforker.commands.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.commands.IdSizes;

public class MethodId extends DataTypeBase {
  public MethodId(ByteBuffer buffer, IdSizes idSizes) {
    super(buffer, idSizes, IdSizes::getMethodIdSize);
  }
}
