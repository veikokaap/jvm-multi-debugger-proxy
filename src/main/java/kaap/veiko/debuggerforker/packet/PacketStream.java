package kaap.veiko.debuggerforker.packet;

import java.io.Closeable;
import java.io.IOException;

public interface PacketStream extends Closeable {
  Packet read() throws IOException;

  void write(Packet packet) throws IOException;
}
