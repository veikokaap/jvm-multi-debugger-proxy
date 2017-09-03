package kaap.veiko.debuggerforker.commands.parser.typeparsers;

import java.lang.reflect.Parameter;
import java.nio.ByteBuffer;

public class ByteParser implements TypeParser<Byte> {
  @Override
  public Byte parse(ByteBuffer byteBuffer, Parameter parameter) throws ReflectiveOperationException {
    return byteBuffer.get();
  }

  @Override
  public boolean hasCorrectType(Class<?> type) {
    return type == byte.class || Byte.class.equals(type);
  }
}
