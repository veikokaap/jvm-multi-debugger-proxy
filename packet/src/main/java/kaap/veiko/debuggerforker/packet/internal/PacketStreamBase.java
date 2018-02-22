package kaap.veiko.debuggerforker.packet.internal;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaap.veiko.debuggerforker.packet.Packet;
import kaap.veiko.debuggerforker.packet.PacketSource;
import kaap.veiko.debuggerforker.packet.PacketStream;

public abstract class PacketStreamBase implements PacketStream {

  private final Logger log = LoggerFactory.getLogger(PacketStreamBase.class);

  private final SocketChannel socketChannel;
  private final PacketSource packetSource;
  private final PacketReader packetReader;
  private final PacketWriter packetWriter;

  public PacketStreamBase(SocketChannel socketChannel, PacketSource.SourceType sourceType) throws IOException {
    socketChannel.configureBlocking(false);
    this.socketChannel = socketChannel;
    this.packetSource = new PacketSource(socketChannel, sourceType);
    this.packetReader = new PacketReader(socketChannel, packetSource);
    this.packetWriter = new PacketWriter(socketChannel);
  }

  public Packet read() throws IOException {
    if (isClosed()) {
      throw new ClosedChannelException();
    }

    try {
      return packetReader.read();
    }
    catch (IOException e) {
      close();
      throw e;
    }
  }

  public void write(Packet packet) throws IOException {
    if (isClosed()) {
      throw new ClosedChannelException();
    }

    try {
      packetWriter.write(packet);
    }
    catch (IOException e) {
      close();
      throw e;
    }
  }

  public SocketChannel getSocketChannel() {
    return socketChannel;
  }

  @Override
  public PacketSource getSource() {
    return packetSource;
  }

  @Override
  public void close() {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PacketStreamBase that = (PacketStreamBase) o;

    return socketChannel.equals(that.socketChannel);
  }

  @Override
  public int hashCode() {
    return socketChannel.hashCode();
  }
}
