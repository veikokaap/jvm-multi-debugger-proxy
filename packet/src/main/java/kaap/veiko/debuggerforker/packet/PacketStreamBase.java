package kaap.veiko.debuggerforker.packet;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class PacketStreamBase implements PacketStream {

  private final Logger log = LoggerFactory.getLogger(PacketStreamBase.class);

  private final SocketChannel socketChannel;
  private final PacketReader packetReader;
  private final PacketWriter packetWriter;
  private final AtomicBoolean closed = new AtomicBoolean(false);

  public PacketStreamBase(SocketChannel socketChannel) throws IOException {
    socketChannel.configureBlocking(false);
    this.socketChannel = socketChannel;
    this.packetReader = new PacketReader(socketChannel);
    this.packetWriter = new PacketWriter(socketChannel);
  }

  public Packet read() throws IOException {
    if (closed.get()) {
      throw new IOException("Stream is closed");
    }

    Packet packet = packetReader.read();
    if (packet != null) {
      packet.setSource(this);
    }
    return packet;
  }

  public void write(Packet packet) throws IOException {
    if (closed.get()) {
      throw new IOException("Stream is closed");
    }

    packetWriter.write(packet);
  }

  @Override
  public void close() throws IOException {
    closed.set(false);
    try {
      socketChannel.close();
    } catch (IOException error) {
      log.error("Error while closing a SocketChannel", error);
    }
  }

  @Override
  public boolean isClosed() {
    return closed.get();
  }
}
