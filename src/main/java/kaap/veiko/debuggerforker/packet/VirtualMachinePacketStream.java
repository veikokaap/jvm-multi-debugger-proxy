package kaap.veiko.debuggerforker.packet;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class VirtualMachinePacketStream extends PacketStreamBase {

  private final ConcurrentMap<Integer, Packet> writtenPackets = new ConcurrentHashMap<>();

  public VirtualMachinePacketStream(SocketChannel socketChannel) throws IOException {
    super(socketChannel, true);
  }

  @Override
  public Packet read() throws IOException {
    Packet readPacket = super.read();
    if (readPacket == null) {
      return null;
    }

    if (readPacket.isReply()) {
      Packet commandPacket = writtenPackets.get(readPacket.getId());

      readPacket.setCommandSet(commandPacket.getCommandSet());
      readPacket.setCommand(commandPacket.getCommand());
    }

    return readPacket;
  }

  @Override
  public void write(Packet packet) throws IOException {
    writtenPackets.put(packet.getId(), packet);

    super.write(packet);
  }
}
