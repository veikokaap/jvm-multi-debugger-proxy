package kaap.veiko.debuggerforker.commands.types;

import java.nio.ByteBuffer;

public interface DataType {
  void putToBuffer(ByteBuffer buffer);
}
