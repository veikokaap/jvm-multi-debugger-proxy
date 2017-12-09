package kaap.veiko.debuggerforker.commands;

import kaap.veiko.debuggerforker.packet.CommandPacket;
import kaap.veiko.debuggerforker.packet.PacketStream;
import kaap.veiko.debuggerforker.packet.ReplyPacket;

class SyntheticPacket implements CommandPacket, ReplyPacket {
  private final int length;
  private final int id;
  private final short flags;
  private final byte[] data;
  private final PacketStream source;

  //ReplyPacket
  private final short errorCode;

  //CommandPacket
  private final short commandSetId;
  private final short commandId;

  SyntheticPacket(int length, int id, short flags, short errorCode, short commandSetId, short commandId,  byte[] data) {
    this.length = length;
    this.id = id;
    this.flags = flags;
    this.data = data;
    this.source = null;
    this.errorCode = errorCode;
    this.commandSetId = commandSetId;
    this.commandId = commandId;
  }

  @Override
  public int getLength() {
    return length;
  }

  @Override
  public int getId() {
    return id;
  }

  @Override
  public short getFlags() {
    return flags;
  }

  @Override
  public boolean isReply() {
    return flags == -128;
  }

  public byte[] getDataBytes() {
    return data;
  }

  @Override
  public short getErrorCode() {
    return errorCode;
  }

  @Override
  public short getCommandSetId() {
    return commandSetId;
  }

  @Override
  public short getCommandId() {
    return commandId;
  }

  @Override
  public CommandPacket getCommandPacket() {
    return this;
  }

  @Override
  public void setCommandPacket(CommandPacket commandPacket) {
  }

  @Override
  public ReplyPacket getReplyPacket() {
    return this;
  }

  @Override
  public void setReplyPacket(ReplyPacket replyPacket) {
  }

  @Override
  public PacketStream getSource() {
    return source;
  }

  @Override
  public boolean isSynthetic() {
    return true;
  }
}
