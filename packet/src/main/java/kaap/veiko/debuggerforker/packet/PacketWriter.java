package kaap.veiko.debuggerforker.packet;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

class PacketWriter {

  private final SocketChannel socketChannel;

  PacketWriter(SocketChannel socketChannel) {
    this.socketChannel = socketChannel;
  }

  void write(Packet packet) throws IOException {
    socketChannel.write(packetToByteBuffer(packet));
  }

  private ByteBuffer packetToByteBuffer(Packet packet) {
    ByteBuffer byteBuffer = ByteBuffer.allocate(packet.getLength());

    byteBuffer.putInt(packet.getLength());
    byteBuffer.putInt(packet.getId());
    byteBuffer.put((byte) packet.getFlags());

    if (packet.isReply()) {
      byteBuffer.putShort(packet.getErrorCode());
    }
    else {
      byteBuffer.put((byte) packet.getCommandSetId());
      byteBuffer.put((byte) packet.getCommandId());
    }

    if (packet.hasData()) {
      byteBuffer.put(packet.getDataBytes());
    }

    byteBuffer.flip();
    return byteBuffer;
  }
}
