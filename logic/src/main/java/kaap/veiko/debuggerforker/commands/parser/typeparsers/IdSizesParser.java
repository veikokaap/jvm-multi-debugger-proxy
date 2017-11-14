package kaap.veiko.debuggerforker.commands.parser.typeparsers;

import java.lang.reflect.Parameter;
import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.types.IdSizes;

public class IdSizesParser implements TypeParser<IdSizes> {
  @Override
  public boolean hasCorrectType(Class<?> type) {
    return IdSizes.class == type;
  }

  @Override
  public void putToBuffer(ByteBuffer buffer, IdSizes value) {
    buffer.putInt(value.getFieldIdSize());
    buffer.putInt(value.getMethodIdSize());
    buffer.putInt(value.getObjectIdSize());
    buffer.putInt(value.getReferenceTypeIdSize());
    buffer.putInt(value.getFrameIdSize());
  }

  @Override
  public IdSizes parse(ByteBuffer byteBuffer, Parameter parameter) throws ReflectiveOperationException {
    return new IdSizes(
        byteBuffer.getInt(),
        byteBuffer.getInt(),
        byteBuffer.getInt(),
        byteBuffer.getInt(),
        byteBuffer.getInt()
    );
  }
}
