package kaap.veiko.debuggerforker.commands.parser.typeparsers;

import java.nio.ByteBuffer;

public class IntParser<T> implements TypeParser<Integer> {
  @Override
  public Integer parse(ByteBuffer byteBuffer, Class<?> type) throws ReflectiveOperationException {
    return byteBuffer.getInt();
  }
}
