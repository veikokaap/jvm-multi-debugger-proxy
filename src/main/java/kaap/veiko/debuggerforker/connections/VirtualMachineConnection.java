package kaap.veiko.debuggerforker.connections;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import kaap.veiko.debuggerforker.packet.PacketStream;
import kaap.veiko.debuggerforker.packet.VirtualMachinePacketStream;

public class VirtualMachineConnection implements Connection {

  private final PacketStream packetStream;

  public VirtualMachineConnection(SocketChannel socketChannel) throws IOException {
    this.packetStream = new VirtualMachinePacketStream(socketChannel);
  }

  public PacketStream getPacketStream() {
    return packetStream;
  }

  @Override
  public void close() throws IOException {
    packetStream.close();
  }
}
