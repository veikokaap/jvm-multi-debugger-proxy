package kaap.veiko.debuggerforker.types;

import java.nio.ByteBuffer;

public interface DataType {
  void putToBuffer(ByteBuffer buffer);
}
