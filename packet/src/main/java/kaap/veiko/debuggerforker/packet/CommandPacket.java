package kaap.veiko.debuggerforker.packet;

import java.util.Arrays;

import kaap.veiko.debuggerforker.packet.internal.PacketBase;

public class CommandPacket extends PacketBase {

  private final short commandId;
  private final short commandSetId;
  private ReplyPacket replyPacket;

  public CommandPacket(int id, short commandSetId, short commandId, byte[] data, PacketSource source) {
    super(id, data, source);
    this.commandSetId = commandSetId;
    this.commandId = commandId;
  }

  public short getCommandId() {
    return commandId;
  }

  public short getCommandSetId() {
    return commandSetId;
  }

  public ReplyPacket getReplyPacket() {
    return replyPacket;
  }

  public void setReplyPacket(ReplyPacket replyPacket) {
    this.replyPacket = replyPacket;
  }

  @Override
  public boolean isReply() {
    return false;
  }

  @Override
  public <T> T visit(PacketVisitor<T> visitor) {
    return visitor.visit(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }

    CommandPacket that = (CommandPacket) o;

    if (commandId != that.commandId) {
      return false;
    }
    if (commandSetId != that.commandSetId) {
      return false;
    }
    return replyPacket != null ? replyPacket.equals(that.replyPacket) : that.replyPacket == null;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (int) commandId;
    result = 31 * result + (int) commandSetId;
    return result;
  }

  @Override
  public String toString() {
    return "CommandPacket{" +
        "length=" + getLength() +
        ", id=" + getId() +
        ", flags=" + getFlags() +
        ", commandId=" + commandId +
        ", commandSetId=" + commandSetId +
        ", data=" + Arrays.toString(getData()) +
        ", source=" + getSource() +
        '}';
  }
}
