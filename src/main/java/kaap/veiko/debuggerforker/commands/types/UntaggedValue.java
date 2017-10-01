package kaap.veiko.debuggerforker.commands.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.commands.sets.virtualmachine.IDSizesReply;

public class UntaggedValue extends Value {
  public UntaggedValue(ByteBuffer byteBuffer, IDSizesReply idSizes, byte typeTag) {
    super(byteBuffer, idSizes, typeTag);
  }
}
