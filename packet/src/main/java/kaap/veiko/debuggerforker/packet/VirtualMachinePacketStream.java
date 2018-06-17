package kaap.veiko.debuggerforker.packet;

import static kaap.veiko.debuggerforker.packet.PacketSource.SourceType.VIRTUAL_MACHINE;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaap.veiko.debuggerforker.packet.internal.PacketStreamBase;
import kaap.veiko.debuggerforker.packet.internal.VmPacketTransformer;

public class VirtualMachinePacketStream extends PacketStreamBase {

  private final Logger log = LoggerFactory.getLogger(VirtualMachinePacketStream.class);

  private final ConcurrentMap<Integer, CommandPacket> writtenCommands = new ConcurrentHashMap<>();
  private final ReplyPacketVisitor outboundPacketVisitor = new ReplyPacketVisitor();

  public VirtualMachinePacketStream(SocketChannel socketChannel) throws IOException {
    super(socketChannel, VIRTUAL_MACHINE, new VmPacketTransformer());
  }

  @Override
  public @Nullable Packet read() throws IOException {
    Packet packet = super.read();
    if (packet == null) {
      return null;
    }

    return packet.visit(outboundPacketVisitor);
  }

  @Override
  public void write(Packet packet) throws IOException {
    if (!packet.isReply() && packet instanceof CommandPacket) {
      writtenCommands.put(packet.getId(), (CommandPacket) packet);
      super.write(packet);
    }
    else {
      log.error("VirtualMachine can't receive replies. Tried to write packet {}", packet);
    }
  }

  private class ReplyPacketVisitor implements PacketVisitor<@Nullable Packet> {
    @Override
    public @Nullable Packet visit(ReplyPacket replyPacket) {
      CommandPacket commandPacket = writtenCommands.get(replyPacket.getId());
      if (commandPacket != null) {
        commandPacket.setReplyPacket(replyPacket);
        replyPacket.setCommandPacket(commandPacket);
      }
      else {
        log.warn("No command packet found for {}", replyPacket);
      }

      return replyPacket;
    }

    @Override
    public Packet visit(CommandPacket packet) {
      return packet;
    }
  }

  @Override
  public String toString() {
    return "VirtualMachinePacketStream{" +
        "socketChannel=" + getSocketChannel() +
        '}';
  }
}
