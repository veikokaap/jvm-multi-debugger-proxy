package kaap.veiko.debuggerforker.commands.parser.typeparsers;

import java.lang.reflect.Parameter;
import java.nio.ByteBuffer;

public interface TypeParser<T> {
  boolean hasCorrectType(Class<?> type);

  void putToBuffer(ByteBuffer buffer, T value);

  T parse(ByteBuffer byteBuffer, Parameter parameter) throws ReflectiveOperationException;
}