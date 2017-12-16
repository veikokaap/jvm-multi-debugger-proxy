package kaap.veiko.debuggerforker.packet.internal;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaap.veiko.debuggerforker.packet.Packet;
import kaap.veiko.debuggerforker.packet.PacketStream;

public abstract class PacketStreamBase implements PacketStream {

  private final Logger log = LoggerFactory.getLogger(PacketStreamBase.class);

  private final SocketChannel socketChannel;
  private final PacketReader packetReader;
  private final PacketWriter packetWriter;

  public PacketStreamBase(SocketChannel socketChannel) throws IOException {
    socketChannel.configureBlocking(false);
    this.socketChannel = socketChannel;
    this.packetReader = new PacketReader(socketChannel, this);
    this.packetWriter = new PacketWriter(socketChannel);
  }

  public Packet read() throws IOException {
    if (isClosed()) {
      throw new IOException("Stream is closed");
    }

    try {
      return packetReader.read();
    }
    catch (ClosedChannelException e) {
      log.error("Received IOException while writing to channel", e);
      close();
      throw e;
    }
  }

  public void write(Packet packet) throws IOException {
    if (isClosed()) {
      throw new IOException("Stream is closed");
    }

    try {
      packetWriter.write(packet);
    }
    catch (IOException e) {
      log.error("Received IOException while writing to channel", e);
      close();
      throw e;
    }
  }

  @Override
  public void close() throws IOException {
    try {
      socketChannel.close();
    }
    catch (IOException error) {
      log.error("Error while closing a SocketChannel", error);
    }
  }

  @Override
  public boolean isClosed() {
    return !socketChannel.isOpen();
  }
}
