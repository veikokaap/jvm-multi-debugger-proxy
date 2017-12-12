package kaap.veiko.debuggerforker.packet;

public interface CommandPacket extends Packet {
  short getCommandSetId();

  short getCommandId();

  ReplyPacket getReplyPacket();

  void setReplyPacket(ReplyPacket replyPacket);

  default <T> T visit(PacketVisitor<T> visitor) {
    return visitor.visit(this);
  }
}
