package kaap.veiko.debuggerforker.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.types.IdSizes;

public class MethodId extends DataTypeBase {
  public MethodId(ByteBuffer buffer, IdSizes idSizes) {
    super(buffer, idSizes, IdSizes::getMethodIdSize);
  }
}
