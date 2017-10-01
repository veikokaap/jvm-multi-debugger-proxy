package kaap.veiko.debuggerforker.commands.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.commands.sets.virtualmachine.IdSizesReply;

public class UntaggedValue extends Value {
  public UntaggedValue(ByteBuffer byteBuffer, IdSizesReply idSizes, byte typeTag) {
    super(byteBuffer, idSizes, typeTag);
  }
}
