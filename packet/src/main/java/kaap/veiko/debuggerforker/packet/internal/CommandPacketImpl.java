package kaap.veiko.debuggerforker.packet.internal;

import java.util.Arrays;

import kaap.veiko.debuggerforker.packet.CommandPacket;
import kaap.veiko.debuggerforker.packet.PacketStream;
import kaap.veiko.debuggerforker.packet.ReplyPacket;

public class CommandPacketImpl extends PacketBase implements CommandPacket {

  private final short commandId;
  private final short commandSetId;
  private ReplyPacket replyPacket;

  public CommandPacketImpl(int length, int id, short flags, short commandSetId, short commandId, byte[] data, PacketStream source) {
    super(length, id, flags, data, source);
    this.commandSetId = commandSetId;
    this.commandId = commandId;
  }

  @Override
  public short getCommandId() {
    return commandId;
  }

  @Override
  public short getCommandSetId() {
    return commandSetId;
  }

  @Override
  public ReplyPacket getReplyPacket() {
    return replyPacket;
  }

  @Override
  public void setReplyPacket(ReplyPacket replyPacket) {
    this.replyPacket = replyPacket;
  }

  @Override
  public boolean isReply() {
    return false;
  }

  @Override
  public String toString() {
    return "CommandPacket{" +
        "length=" + getLength() +
        ", id=" + getId() +
        ", flags=" + getFlags() +
        ", commandId=" + commandId +
        ", commandSetId=" + commandSetId +
        ", data=" + Arrays.toString(getDataBytes()) +
        ", source=" + getSource() +
        '}';
  }
}
