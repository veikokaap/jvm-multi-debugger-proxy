package kaap.veiko.debuggerforker.commands.parser.typeparsers;

import java.nio.ByteBuffer;

public class ShortParser<T> implements TypeParser<Short> {
  @Override
  public Short parse(ByteBuffer byteBuffer, Class<?> type) throws ReflectiveOperationException {
    return byteBuffer.getShort();
  }
}
