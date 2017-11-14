package kaap.veiko.debuggerforker.types.parsers;

import java.lang.reflect.Parameter;
import java.nio.ByteBuffer;

public class IntParser implements TypeParser<Integer> {
  @Override
  public Integer parse(ByteBuffer byteBuffer, Parameter parameter) throws ReflectiveOperationException {
    return byteBuffer.getInt();
  }

  @Override
  public boolean hasCorrectType(Class<?> type) {
    return type == int.class || Integer.class.equals(type);
  }

  @Override
  public void putToBuffer(ByteBuffer buffer, Integer value) {
    buffer.putInt(value);
  }
}
