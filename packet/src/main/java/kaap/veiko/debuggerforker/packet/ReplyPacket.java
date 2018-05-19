package kaap.veiko.debuggerforker.packet;

import java.util.Arrays;

import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import kaap.veiko.debuggerforker.packet.internal.PacketBase;

public class ReplyPacket extends PacketBase {

  private final short errorCode;
  private @MonotonicNonNull CommandPacket commandPacket = null;

  public ReplyPacket(int id, short errorCode, byte[] data, PacketSource source) {
    super(id, data, source);
    this.errorCode = errorCode;
  }

  public short getErrorCode() {
    return errorCode;
  }

  public @Nullable CommandPacket getCommandPacket() {
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

    ReplyPacket that = (ReplyPacket) o;

    return errorCode == that.errorCode;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (int) errorCode;
    return result;
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
