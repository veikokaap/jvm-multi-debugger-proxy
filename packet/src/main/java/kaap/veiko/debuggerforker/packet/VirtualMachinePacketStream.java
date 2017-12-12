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
  private final ReplyPacketVisitor replyPacketVisitor = new ReplyPacketVisitor();

  public VirtualMachinePacketStream(SocketChannel socketChannel) throws IOException {
    super(socketChannel);
  }

  @Override
  public Packet read() throws IOException {
    Packet packet = super.read();
    if (packet == null) {
      return null;
    }

    return packet.visit(replyPacketVisitor);
  }

  @Override
  public void write(Packet packet) throws IOException {
    writtenPackets.put(packet.getId(), (CommandPacket) packet);
    super.write(packet);
  }

  private class ReplyPacketVisitor implements PacketVisitor<Packet> {
    @Override
    public Packet visit(ReplyPacket replyPacket) {
      CommandPacket commandPacket = writtenPackets.get(replyPacket.getId());
      commandPacket.setReplyPacket(replyPacket);
      replyPacket.setCommandPacket(commandPacket);

      return replyPacket;
    }

    @Override
    public Packet visit(CommandPacket packet) {
      return packet;
    }
  }
}
