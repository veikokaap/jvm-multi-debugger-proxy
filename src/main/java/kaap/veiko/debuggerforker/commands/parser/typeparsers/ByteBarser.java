package kaap.veiko.debuggerforker.commands.parser.typeparsers;

import java.nio.ByteBuffer;

public class ByteBarser implements TypeParser<Byte> {
  @Override
  public Byte parse(ByteBuffer byteBuffer, Class<?> type) throws ReflectiveOperationException {
    return byteBuffer.get();
  }
}
