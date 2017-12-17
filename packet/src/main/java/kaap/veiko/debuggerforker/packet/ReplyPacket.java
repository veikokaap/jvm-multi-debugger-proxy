package kaap.veiko.debuggerforker.packet;

import java.util.Arrays;

import kaap.veiko.debuggerforker.packet.internal.PacketBase;

public class ReplyPacket extends PacketBase {

  private final short errorCode;
  private CommandPacket commandPacket;

  public ReplyPacket(int id, short errorCode, byte[] data, PacketStream source) {
    super(id, data, source);
    this.errorCode = errorCode;
  }

  public short getErrorCode() {
    return errorCode;
  }

  public CommandPacket getCommandPacket() {
    return commandPacket;
  }

  public void setCommandPacket(CommandPacket commandPacket) {
    this.commandPacket = commandPacket;
  }

  @Override
  public boolean isReply() {
    return true;
  }

  @Override
  public <T> T visit(PacketVisitor<T> visitor) {
    return visitor.visit(this);
  }

  @Override
  public String toString() {
    return "ReplyPacket{" +
        "length=" + getLength() +
        ", id=" + getId() +
        ", flags=" + getFlags() +
        ", errorCode=" + errorCode +
        ", data=" + Arrays.toString(getData()) +
        ", source=" + getSource() +
        '}';
  }
}
