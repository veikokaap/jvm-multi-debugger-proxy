package kaap.veiko.debuggerforker.commands.parser.typeparsers;

import java.lang.reflect.Parameter;
import java.nio.ByteBuffer;

public class BooleanParser implements TypeParser<Boolean> {
  @Override
  public Boolean parse(ByteBuffer byteBuffer, Parameter parameter) throws ReflectiveOperationException {
    return byteBuffer.get() != 0;
  }

  @Override
  public boolean hasCorrectType(Class<?> type) {
    return type == boolean.class || Boolean.class.equals(type);
  }

  @Override
  public void putToBuffer(ByteBuffer buffer, Boolean value) {
    if (value) {
      buffer.put((byte) 1);
    }
    else {
      buffer.put((byte) 0);
    }
  }
}
