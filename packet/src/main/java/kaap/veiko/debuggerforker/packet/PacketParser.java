package kaap.veiko.debuggerforker.packet;

import java.nio.ByteBuffer;
import java.util.function.Function;

class PacketParser {

  private final int length;
  private final byte[] packetBytes;
  private int index = 0;

  private PacketParser(int length, byte[] packetBytes) {
    this.length = length;
    this.packetBytes = packetBytes;
  }

  static Packet parse(int length, byte[] packetBytes) {
    return new PacketParser(length, packetBytes).internalParse();
  }

  private Packet internalParse() {
    Packet packet = new PacketImpl();
    packet.setLength(length);

    packet.setId(readInt());
    packet.setFlags(readByte());

    if (packet.isReply()) {
      packet.setErrorCode(readShort());
    }
    else {
      packet.setCommandSetId(readByte());
      packet.setCommandId(readByte());
    }

    if (packet.hasData()) {
      packet.setDataBytes(readData());
    }

    return packet;
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
    return readBytesAndApply(length - Packet.HEADER_LENGTH, bf -> {
      byte[] data = new byte[length - Packet.HEADER_LENGTH];
      bf.get(data);
      return data;
    });
  }

  private <T> T readBytesAndApply(int nrOfBytes, Function<ByteBuffer, T> function) {
    ByteBuffer buf = ByteBuffer.wrap(packetBytes, index, nrOfBytes);
    index += nrOfBytes;
    return function.apply(buf);
  }
}
