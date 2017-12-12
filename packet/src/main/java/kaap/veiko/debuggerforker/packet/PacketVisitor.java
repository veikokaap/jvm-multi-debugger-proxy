package kaap.veiko.debuggerforker.packet;

public interface PacketVisitor<T> {
  T visit(ReplyPacket replyPacket);

  T visit(CommandPacket commandPacket);
}
