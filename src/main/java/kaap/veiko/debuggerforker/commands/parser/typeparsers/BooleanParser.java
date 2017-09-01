package kaap.veiko.debuggerforker.commands.parser.typeparsers;

import java.nio.ByteBuffer;

public class BooleanParser<T> implements TypeParser<Boolean> {
  @Override
  public Boolean parse(ByteBuffer byteBuffer, Class<?> type) throws ReflectiveOperationException {
    return byteBuffer.get() != 0;
  }
}
