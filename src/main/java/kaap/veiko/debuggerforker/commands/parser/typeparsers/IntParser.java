package kaap.veiko.debuggerforker.commands.parser.typeparsers;

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
}