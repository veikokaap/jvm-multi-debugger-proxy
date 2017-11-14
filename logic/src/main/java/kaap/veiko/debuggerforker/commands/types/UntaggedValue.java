package kaap.veiko.debuggerforker.commands.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.commands.IdSizes;

public class UntaggedValue extends Value {
  public UntaggedValue(ByteBuffer byteBuffer, IdSizes idSizes, byte typeTag) {
    super(byteBuffer, idSizes, typeTag);
  }
}
