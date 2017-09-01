package kaap.veiko.debuggerforker.commands.parser.typeparsers;

import java.nio.ByteBuffer;

@FunctionalInterface
public interface TypeParser<T> {
  T parse(ByteBuffer byteBuffer, Class<?> type) throws ReflectiveOperationException;
}