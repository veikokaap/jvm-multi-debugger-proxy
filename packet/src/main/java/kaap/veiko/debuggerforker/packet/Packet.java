package kaap.veiko.debuggerforker.packet;

public interface Packet {
  int HEADER_LENGTH = 11;

  int getLength();

  int getId();

  short getFlags();

  byte[] getData();

  boolean isReply();

  default boolean hasData() {
    return getLength() > HEADER_LENGTH;
  }

  PacketSource getSource();

  boolean isSynthetic();

  <T> T visit(PacketVisitor<T> visitor);
}
