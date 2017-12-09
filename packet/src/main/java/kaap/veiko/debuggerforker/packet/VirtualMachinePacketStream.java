package kaap.veiko.debuggerforker.packet;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import kaap.veiko.debuggerforker.packet.internal.PacketTransformer;
import kaap.veiko.debuggerforker.packet.internal.PacketStreamBase;

public class VirtualMachinePacketStream extends PacketStreamBase {

  private final ConcurrentMap<Integer, CommandPacket> writtenPackets = new ConcurrentHashMap<>();
  private final PacketTransformer packetTransformer = new PacketTransformer();

  public VirtualMachinePacketStream(SocketChannel socketChannel) throws IOException {
    super(socketChannel);
  }

  @Override
  public Packet read() throws IOException {
    Packet readPacket = super.read();
    if (readPacket == null) {
      return null;
    }

    if (readPacket.isReply()) {
      CommandPacket commandPacket = writtenPackets.get(readPacket.getId());
      ReplyPacket replyPacket = (ReplyPacket) readPacket;

      commandPacket.setReplyPacket(replyPacket);
      replyPacket.setCommandPacket(commandPacket);
    }

    return readPacket;
  }

  @Override
  public void write(Packet packet) throws IOException {
    writtenPackets.put(packet.getId(), (CommandPacket) packet);
    super.write(packet);
  }
}
