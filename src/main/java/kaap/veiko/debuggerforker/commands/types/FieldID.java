package kaap.veiko.debuggerforker.commands.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.commands.sets.virtualmachine.IDSizesReply;

public class FieldID extends DataTypeBase {
  public FieldID(ByteBuffer buffer, IDSizesReply idSizes) {
    super(buffer, idSizes, IDSizesReply::getFieldIDSize);
  }
}
