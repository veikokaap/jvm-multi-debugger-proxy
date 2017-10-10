package kaap.veiko.debuggerforker.commands.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.commands.IdSizes;
import kaap.veiko.debuggerforker.commands.constants.Tag;
import kaap.veiko.debuggerforker.utils.ByteBufferUtil;

public class Value implements DataType {
  private final Tag type;
  private final Long value;

  public Value(ByteBuffer byteBuffer, IdSizes idSizes) {
    this(byteBuffer, idSizes, byteBuffer.get());
  }

  protected Value(ByteBuffer byteBuffer, IdSizes idSizes, byte typeTag) {
    this.type = Tag.findByValue(typeTag);
    Integer size = type.getSize();

    if (size == null) {
      value = ByteBufferUtil.getLong(byteBuffer, idSizes.getObjectIdSize());
    }
    else if (size == 0) {
      value = null;
    }
    else {
      value = ByteBufferUtil.getLong(byteBuffer, size);
    }
  }

  public Tag getType() {
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
