package kaap.veiko.debuggerforker.connections;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import kaap.veiko.debuggerforker.packet.DebuggerPacketStream;
import kaap.veiko.debuggerforker.packet.Packet;
import kaap.veiko.debuggerforker.packet.PacketIdTransformer;
import kaap.veiko.debuggerforker.packet.PacketStream;

public class DebuggerManager implements Connection {

  private final DebuggerPacketStream packetStream;

  public DebuggerManager(SocketChannel socketChannel, PacketIdTransformer packetIdTransformer) throws IOException {
    packetStream = new DebuggerPacketStream(socketChannel, packetIdTransformer);
  }

  @Override
  public PacketStream getPacketStream() {
    return packetStream;
  }

  @Override
  public void close() throws Exception {
    packetStream.close();
  }

  public boolean isMyReply(Packet packet) {
    return packetStream.isMyReply(packet);
  }

  public void sendPacket(Packet packet) throws IOException {
    packetStream.write(packet);
  }
}
