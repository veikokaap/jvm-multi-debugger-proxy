package kaap.veiko.debuggerforker.commands.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.commands.sets.virtualmachine.IDSizesReply;

public class MethodID extends DataTypeBase {
  public MethodID(ByteBuffer buffer, IDSizesReply idSizes) {
    super(buffer, idSizes, IDSizesReply::getMethodIDSize);
  }
}
