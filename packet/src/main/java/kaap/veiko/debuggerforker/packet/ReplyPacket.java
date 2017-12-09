package kaap.veiko.debuggerforker.packet;

public interface ReplyPacket extends Packet {
  short getErrorCode();

  CommandPacket getCommandPacket();

  void setCommandPacket(CommandPacket commandPacket);
}
