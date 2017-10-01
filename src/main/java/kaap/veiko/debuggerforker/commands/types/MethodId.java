package kaap.veiko.debuggerforker.commands.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.commands.sets.virtualmachine.IdSizesReply;

public class MethodId extends DataTypeBase {
  public MethodId(ByteBuffer buffer, IdSizesReply idSizes) {
    super(buffer, idSizes, IdSizesReply::getMethodIdSize);
  }
}
