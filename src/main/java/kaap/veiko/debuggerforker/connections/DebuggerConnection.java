package kaap.veiko.debuggerforker.connections;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import kaap.veiko.debuggerforker.packet.DebuggerPacketStream;
import kaap.veiko.debuggerforker.packet.PacketStream;

public class DebuggerConnection implements Connection {

  private final DebuggerPacketStream packetStream;

  public DebuggerConnection(SocketChannel socketChannel) throws IOException {
    packetStream = new DebuggerPacketStream(socketChannel);
  }

  @Override
  public PacketStream getPacketStream() {
    return packetStream;
  }

  @Override
  public void close() throws Exception {
    packetStream.close();
  }
}
