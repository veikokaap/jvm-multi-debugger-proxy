package kaap.veiko.debuggerforker.packet;

public interface ReplyPacket extends Packet {
  short getErrorCode();

  CommandPacket getCommandPacket();

  void setCommandPacket(CommandPacket commandPacket);

  @Override
  default <T> T visit(PacketVisitor<T> visitor) {
    return visitor.visit(this);
  }
}
