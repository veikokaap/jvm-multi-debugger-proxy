package kaap.veiko.debuggerforker.commands.parser.typeparsers;

import java.lang.reflect.Parameter;
import java.nio.ByteBuffer;

public class LongParser implements TypeParser<Long> {
  @Override
  public Long parse(ByteBuffer byteBuffer, Parameter parameter) throws ReflectiveOperationException {
    return byteBuffer.getLong();
  }

  @Override
  public boolean hasCorrectType(Class<?> type) {
    return type == long.class || Long.class.equals(type);
  }
}
