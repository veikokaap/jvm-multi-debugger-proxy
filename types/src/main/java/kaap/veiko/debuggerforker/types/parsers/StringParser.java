package kaap.veiko.debuggerforker.types.parsers;

import java.lang.reflect.Parameter;
import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.utils.ByteBufferUtil;

public class StringParser implements TypeParser<String> {
  @Override
  public String parse(ByteBuffer byteBuffer, Parameter parameter) throws ReflectiveOperationException {
    return ByteBufferUtil.getString(byteBuffer);
  }

  public void putToBuffer(ByteBuffer buffer, String value) {
    ByteBufferUtil.putString(buffer, value);
  }

  @Override
  public boolean hasCorrectType(Class<?> type) {
    return String.class.equals(type);
  }
}
