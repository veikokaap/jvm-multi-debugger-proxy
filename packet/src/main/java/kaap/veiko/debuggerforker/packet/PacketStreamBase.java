package kaap.veiko.debuggerforker.packet;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public abstract class PacketStreamBase implements PacketStream {

  private final SocketChannel socketChannel;
  private final PacketReader packetReader;
  private final PacketWriter packetWriter;

  public PacketStreamBase(SocketChannel socketChannel) throws IOException {
    socketChannel.configureBlocking(false);
    this.socketChannel = socketChannel;
    this.packetReader = new PacketReader(socketChannel);
    this.packetWriter = new PacketWriter(socketChannel);
  }

  public Packet read() throws IOException {
    Packet packet = packetReader.read();
    if (packet != null) {
      packet.setSource(this);
    }
    return packet;
  }

  public void write(Packet packet) throws IOException {
    packetWriter.write(packet);
  }

  @Override
  public void close() throws IOException {
    socketChannel.close();
  }
}
