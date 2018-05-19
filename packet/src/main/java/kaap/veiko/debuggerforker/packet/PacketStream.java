package kaap.veiko.debuggerforker.packet;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface PacketStream {
  @Nullable Packet read() throws IOException;

  void write(Packet packet) throws IOException;

  SocketChannel getSocketChannel();

  PacketSource getSource();

  void close();

  boolean isClosed();
}
