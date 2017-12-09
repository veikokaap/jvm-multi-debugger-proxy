package kaap.veiko.debuggerforker.packet.internal;

import java.util.Arrays;

import kaap.veiko.debuggerforker.packet.CommandPacket;
import kaap.veiko.debuggerforker.packet.PacketStream;
import kaap.veiko.debuggerforker.packet.ReplyPacket;

public class ReplyPacketImpl extends PacketBase implements ReplyPacket {

  private final short errorCode;
  private CommandPacket commandPacket;

  public ReplyPacketImpl(int length, int id, short flags, short errorCode, byte[] data, PacketStream source) {
    super(length, id, flags, data, source);
    this.errorCode = errorCode;
  }

  @Override
  public short getErrorCode() {
    return 0;
  }

  @Override
  public CommandPacket getCommandPacket() {
    return commandPacket;
  }

  @Override
  public void setCommandPacket(CommandPacket commandPacket) {
    this.commandPacket = commandPacket;
  }

  @Override
  public boolean isReply() {
    return true;
  }

  @Override
  public String toString() {
    return "ReplyPacket{" +
        "length=" + getLength() +
        ", id=" + getId() +
        ", flags=" + getFlags() +
        ", errorCode=" + errorCode +
        ", data=" + Arrays.toString(getDataBytes()) +
        ", source=" + getSource() +
        '}';
  }
}
