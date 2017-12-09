package kaap.veiko.debuggerforker.packet;

public interface Packet {
  int HEADER_LENGTH = 11;

  int getLength();

  int getId();

  short getFlags();

  byte[] getDataBytes();

  boolean isReply();

  default boolean hasData() {
    return getLength() > HEADER_LENGTH;
  }

  PacketStream getSource();

  boolean isSynthetic();
}
