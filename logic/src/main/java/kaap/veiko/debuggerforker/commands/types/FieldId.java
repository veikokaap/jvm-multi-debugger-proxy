package kaap.veiko.debuggerforker.commands.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.commands.IdSizes;

public class FieldId extends DataTypeBase {
  public FieldId(ByteBuffer buffer, IdSizes idSizes) {
    super(buffer, idSizes, IdSizes::getFieldIdSize);
  }
}
