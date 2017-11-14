package kaap.veiko.debuggerforker.types.parsers;

import java.lang.reflect.Parameter;
import java.nio.ByteBuffer;

public class ShortParser implements TypeParser<Short> {
  @Override
  public Short parse(ByteBuffer byteBuffer, Parameter parameter) throws ReflectiveOperationException {
    return byteBuffer.getShort();
  }

  @Override
  public boolean hasCorrectType(Class<?> type) {
    return type == short.class || Short.class.equals(type);
  }

  @Override
  public void putToBuffer(ByteBuffer buffer, Short value) {
    buffer.putShort(value);
  }
}
