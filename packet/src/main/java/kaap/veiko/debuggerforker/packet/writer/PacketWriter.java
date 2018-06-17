package kaap.veiko.debuggerforker.packet.writer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import kaap.veiko.debuggerforker.packet.Packet;

public class PacketWriter {

  private final SocketChannel socketChannel;
  private final PacketToByteBufferWriter packetToByteBufferWriter = new PacketToByteBufferWriter();

  public PacketWriter(SocketChannel socketChannel) {
    this.socketChannel = socketChannel;
  }

  public void write(Packet packet) throws IOException {
    ByteBuffer buffer = packet.visit(packetToByteBufferWriter);
    while (buffer.hasRemaining()) {
      socketChannel.write(buffer);
    }
  }
}
