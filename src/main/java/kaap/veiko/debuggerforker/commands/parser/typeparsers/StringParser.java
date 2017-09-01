package kaap.veiko.debuggerforker.commands.parser.typeparsers;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.utils.ByteBufferUtil;

public class StringParser<T> implements TypeParser<String> {
  @Override
  public String parse(ByteBuffer byteBuffer, Class<?> type) throws ReflectiveOperationException {
    return ByteBufferUtil.getString(byteBuffer);
  }
}
