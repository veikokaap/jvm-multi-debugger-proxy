package kaap.veiko.debuggerforker.packet.internal;

import java.nio.ByteBuffer;
import java.util.function.Function;

import kaap.veiko.debuggerforker.packet.CommandPacket;
import kaap.veiko.debuggerforker.packet.Packet;
import kaap.veiko.debuggerforker.packet.PacketSource;
import kaap.veiko.debuggerforker.packet.PacketStream;
import kaap.veiko.debuggerforker.packet.ReplyPacket;

class PacketParser {

  private final int length;
  private final byte[] packetBytes;
  private int index = 0;

  private PacketParser(int length, byte[] packetBytes) {
    this.length = length;
    this.packetBytes = packetBytes;
  }

  static Packet parse(int length, byte[] packetBytes, PacketSource source) {
    return new PacketParser(length, packetBytes).internalParse(source);
  }

  private Packet internalParse(PacketSource source) {
    int id = readInt();
    short flags = readByte();

    if (flags == -128) {
      short errorCode = readShort();
      byte[] data = readData();
      return new ReplyPacket(id, errorCode, data, source);
    }
    else {
      short commandSetId = readByte();
      short commandId = readByte();
      byte[] data = readData();

      return new CommandPacket(id, commandSetId, commandId, data, source);
    }
  }

  private int readInt() {
    return readBytesAndApply(4, ByteBuffer::getInt);
  }

  private short readShort() {
    return readBytesAndApply(2, ByteBuffer::getShort);
  }

  private short readByte() {
    return (short) readBytesAndApply(1, ByteBuffer::get);
  }

  private byte[] readData() {
    if (length > Packet.HEADER_LENGTH) {
      return readBytesAndApply(length - Packet.HEADER_LENGTH, bf -> {
        byte[] data = new byte[length - Packet.HEADER_LENGTH];
        bf.get(data);
        return data;
      });
    } else {
      return new byte[0];
    }
  }

  private <T> T readBytesAndApply(int nrOfBytes, Function<ByteBuffer, T> function) {
    ByteBuffer buf = ByteBuffer.wrap(packetBytes, index, nrOfBytes);
    index += nrOfBytes;
    return function.apply(buf);
  }
}
