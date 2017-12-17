package kaap.veiko.debuggerforker.commands;

import kaap.veiko.debuggerforker.commands.parser.CommandType;
import kaap.veiko.debuggerforker.commands.sets.CommandIdentifier;
import kaap.veiko.debuggerforker.packet.CommandPacket;
import kaap.veiko.debuggerforker.packet.Packet;
import kaap.veiko.debuggerforker.packet.PacketStream;
import kaap.veiko.debuggerforker.packet.PacketVisitor;
import kaap.veiko.debuggerforker.packet.ReplyPacket;

public class SyntheticPacket implements CommandPacket, ReplyPacket {
  private final int id;
  private final short flags;
  private byte[] data = new byte[0];
  private final PacketStream source;

  //ReplyPacket
  private final short errorCode;

  //CommandPacket
  private final short commandSetId;
  private final short commandId;

  public static SyntheticPacket create(int packetId, CommandIdentifier identifier) {
    if (identifier.getType() == CommandType.REPLY) {
      return new SyntheticPacket(packetId, (short) -128, (short) 0, identifier.getCommandSetId(), identifier.getCommandId());
    } else {
      return new SyntheticPacket(packetId, (short)0, (short)0, identifier.getCommandSetId(), identifier.getCommandId());
    }
  }

  private SyntheticPacket(int id, short flags, short errorCode, short commandSetId, short commandId) {
    this.id = id;
    this.flags = flags;
    this.source = null;
    this.errorCode = errorCode;
    this.commandSetId = commandSetId;
    this.commandId = commandId;
  }

  @Override
  public int getLength() {
    return Packet.HEADER_LENGTH + data.length;
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

  public void setDataBytes(byte[] data) {
    this.data = data;
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
  public <T> T visit(PacketVisitor<T> visitor) {
    if (isReply()) {
      return visitor.visit((ReplyPacket) this);
    }
    else {
      return visitor.visit((CommandPacket) this);
    }
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
