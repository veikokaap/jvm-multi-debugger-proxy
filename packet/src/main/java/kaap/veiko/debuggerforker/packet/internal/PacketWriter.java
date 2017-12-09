package kaap.veiko.debuggerforker.packet.internal;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import kaap.veiko.debuggerforker.packet.CommandPacket;
import kaap.veiko.debuggerforker.packet.Packet;
import kaap.veiko.debuggerforker.packet.ReplyPacket;

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
      ReplyPacket replyPacket = (ReplyPacket) packet;
      byteBuffer.putShort(replyPacket.getErrorCode());
    }
    else {
      CommandPacket commandPacket = (CommandPacket) packet;
      byteBuffer.put((byte) commandPacket.getCommandSetId());
      byteBuffer.put((byte) commandPacket.getCommandId());
    }

    if (packet.hasData()) {
      byteBuffer.put(packet.getDataBytes());
    }

    byteBuffer.flip();
    return byteBuffer;
  }
}
