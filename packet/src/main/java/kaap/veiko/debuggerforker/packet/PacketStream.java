package kaap.veiko.debuggerforker.packet;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public interface PacketStream {
  Packet read() throws IOException;

  void write(Packet packet) throws IOException;

  SocketChannel getSocketChannel();

  void close();

  boolean isClosed();
}
