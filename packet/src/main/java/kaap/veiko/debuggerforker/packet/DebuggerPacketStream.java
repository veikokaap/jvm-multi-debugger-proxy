package kaap.veiko.debuggerforker.packet;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

public class DebuggerPacketStream extends PacketStreamBase {

  private final List<Integer> myNewIds = new ArrayList<>();
  private final PacketIdTransformer packetIdTransformer;

  public DebuggerPacketStream(SocketChannel socketChannel, PacketIdTransformer packetIdTransformer) throws IOException {
    super(socketChannel);
    this.packetIdTransformer = packetIdTransformer;
  }

  @Override
  public Packet read() throws IOException {
    Packet packet = super.read();
    if (packet == null) {
      return null;
    }

    int originalId = packet.getId();
    int newId = packetIdTransformer.getNewId(originalId);

    myNewIds.add(newId);
    packet.setId(newId);

    return packet;
  }

  @Override
  public void write(Packet packet) throws IOException {
    if (packet.isReply()) {
      int originalId = packetIdTransformer.getOriginalId(packet.getId());
      packet.setId(originalId);
    }

    super.write(packet);
  }

  public boolean isMyReply(Packet packet) {
    if (!packet.isReply()) {
      throw new UnsupportedOperationException("Passed command packet as a reply packet");
    }
    return myNewIds.contains(packet.getId());
  }
}
