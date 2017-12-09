package kaap.veiko.debuggerforker.packet;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import kaap.veiko.debuggerforker.packet.internal.PacketTransformer;
import kaap.veiko.debuggerforker.packet.internal.PacketStreamBase;

public class DebuggerPacketStream extends PacketStreamBase {

  private final List<Integer> myNewIds = new ArrayList<>();
  private final PacketTransformer packetTransformer;

  public DebuggerPacketStream(SocketChannel socketChannel, PacketTransformer packetTransformer) throws IOException {
    super(socketChannel);
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
}
