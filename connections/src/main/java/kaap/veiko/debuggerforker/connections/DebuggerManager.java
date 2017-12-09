package kaap.veiko.debuggerforker.connections;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import kaap.veiko.debuggerforker.packet.DebuggerPacketStream;
import kaap.veiko.debuggerforker.packet.Packet;
import kaap.veiko.debuggerforker.packet.internal.PacketTransformer;
import kaap.veiko.debuggerforker.packet.PacketStream;

public class DebuggerManager implements Connection {

  private final DebuggerPacketStream packetStream;

  public DebuggerManager(SocketChannel socketChannel, PacketTransformer packetTransformer) throws IOException {
    packetStream = new DebuggerPacketStream(socketChannel, packetTransformer);
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
