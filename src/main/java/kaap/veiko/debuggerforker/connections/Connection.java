package kaap.veiko.debuggerforker.connections;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import kaap.veiko.debuggerforker.packet.PacketStream;

abstract class Connection implements AutoCloseable {

  private final SocketChannel socketChannel;
  private final PacketStream packetStream;

  Connection(SocketChannel socketChannel, boolean virtualMachineConnection) throws IOException {
    this.socketChannel = socketChannel;
    this.packetStream = new PacketStream(socketChannel, virtualMachineConnection);
  }

  public PacketStream getPacketStream() {
    return packetStream;
  }

  @Override
  public void close() throws IOException {
    socketChannel.close();
  }
}
