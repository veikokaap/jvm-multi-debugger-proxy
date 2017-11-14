package kaap.veiko.debuggerforker.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.types.IdSizes;

public class FieldId extends DataTypeBase {
  public FieldId(ByteBuffer buffer, IdSizes idSizes) {
    super(buffer, idSizes, IdSizes::getFieldIdSize);
  }
}
