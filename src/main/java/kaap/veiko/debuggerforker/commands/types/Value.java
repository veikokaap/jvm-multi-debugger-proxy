package kaap.veiko.debuggerforker.commands.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.commands.constants.TypeTag;
import kaap.veiko.debuggerforker.commands.sets.virtualmachine.IDSizesReplyCommand;
import kaap.veiko.debuggerforker.utils.ByteBufferUtil;

public class Value implements DataType {
  private final TypeTag type;
  private final Long value;

  public Value(ByteBuffer byteBuffer, IDSizesReplyCommand idSizes) {
    this(byteBuffer, idSizes, byteBuffer.get());
  }

  protected Value(ByteBuffer byteBuffer, IDSizesReplyCommand idSizes, byte typeTag) {
    this.type = TypeTag.findByValue(typeTag);
    Integer size = type.getSize();

    if (size == null) {
      value = ByteBufferUtil.getLong(byteBuffer, idSizes.getObjectIDSize());
    }
    else if (size == 0) {
      value = null;
    }
    else {
      value = ByteBufferUtil.getLong(byteBuffer, size);
    }
  }

  public TypeTag getType() {
    return type;
  }

  public Long getValue() {
    return value;
  }

  @Override
  public long asLong() {
    return getValue();
  }
}
