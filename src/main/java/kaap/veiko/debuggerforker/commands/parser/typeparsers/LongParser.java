package kaap.veiko.debuggerforker.commands.parser.typeparsers;

import java.nio.ByteBuffer;

public class LongParser<T> implements TypeParser<Long> {
  @Override
  public Long parse(ByteBuffer byteBuffer, Class<?> type) throws ReflectiveOperationException {
    return byteBuffer.getLong();
  }
}
