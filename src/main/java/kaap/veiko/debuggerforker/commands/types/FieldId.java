package kaap.veiko.debuggerforker.commands.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.commands.sets.virtualmachine.IdSizesReply;

public class FieldId extends DataTypeBase {
  public FieldId(ByteBuffer buffer, IdSizesReply idSizes) {
    super(buffer, idSizes, IdSizesReply::getFieldIdSize);
  }
}
