package kaap.veiko.debuggerforker.packet.internal;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import kaap.veiko.debuggerforker.packet.CommandPacket;
import kaap.veiko.debuggerforker.packet.Packet;
import kaap.veiko.debuggerforker.packet.PacketVisitor;
import kaap.veiko.debuggerforker.packet.ReplyPacket;

class PacketWriter {

  private final SocketChannel socketChannel;
  private final PacketByteBufferConverter packetByteBufferConverter = new PacketByteBufferConverter();

  PacketWriter(SocketChannel socketChannel) {
    this.socketChannel = socketChannel;
  }

  void write(Packet packet) throws IOException {
    ByteBuffer buffer = packet.visit(packetByteBufferConverter);
    while (buffer.hasRemaining()) {
      socketChannel.write(buffer);
    }
  }

  private class PacketByteBufferConverter implements PacketVisitor<ByteBuffer> {

    private void writeHeader(Packet packet, ByteBuffer byteBuffer) {
      byteBuffer.putInt(packet.getLength());
      byteBuffer.putInt(packet.getId());
      byteBuffer.put((byte) packet.getFlags());
    }

    private void writeData(Packet packet, ByteBuffer byteBuffer) {
      if (packet.hasData()) {
        byteBuffer.put(packet.getData());
      }
    }

    @Override
    public ByteBuffer visit(ReplyPacket packet) {
      ByteBuffer byteBuffer = ByteBuffer.allocate(packet.getLength());

      writeHeader(packet, byteBuffer);
      byteBuffer.putShort(packet.getErrorCode());

      writeData(packet, byteBuffer);

      byteBuffer.flip();
      return byteBuffer;
    }

    @Override
    public ByteBuffer visit(CommandPacket packet) {
      ByteBuffer byteBuffer = ByteBuffer.allocate(packet.getLength());
      writeHeader(packet, byteBuffer);

      byteBuffer.put((byte) packet.getCommandSetId());
      byteBuffer.put((byte) packet.getCommandId());

      writeData(packet, byteBuffer);

      byteBuffer.flip();
      return byteBuffer;
    }
  }
}
