package kaap.veiko.debuggerforker.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.types.IdSizes;

public class UntaggedValue extends Value {
  public UntaggedValue(ByteBuffer byteBuffer, IdSizes idSizes, byte typeTag) {
    super(byteBuffer, idSizes, typeTag);
  }
}
