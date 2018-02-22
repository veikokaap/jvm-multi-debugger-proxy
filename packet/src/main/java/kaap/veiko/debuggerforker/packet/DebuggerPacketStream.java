package kaap.veiko.debuggerforker.packet;

import static kaap.veiko.debuggerforker.packet.PacketSource.SourceType.DEBUGGER;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import kaap.veiko.debuggerforker.packet.internal.PacketTransformer;
import kaap.veiko.debuggerforker.packet.internal.PacketStreamBase;

public class DebuggerPacketStream extends PacketStreamBase {

  private final PacketTransformer packetTransformer;

  public DebuggerPacketStream(SocketChannel socketChannel, PacketTransformer packetTransformer) throws IOException {
    super(socketChannel, DEBUGGER);
    this.packetTransformer = packetTransformer;
  }

  @Override
  public Packet read() throws IOException {
    Packet packet = super.read();
    if (packet == null) {
      return null;
    }

    return packetTransformer.transformReadPacket(packet);
  }

  @Override
  public void write(Packet packet) throws IOException {
    Packet transformed = packetTransformer.transformWritePacket(packet);
    super.write(transformed);
  }

  @Override
  public String toString() {
    return "DebuggerPacketStream{" +
        "socketChannel=" + getSocketChannel() +
        '}';
  }
}
