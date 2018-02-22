package kaap.veiko.debuggerforker.packet.internal;

import java.util.Arrays;
import java.util.Objects;

import kaap.veiko.debuggerforker.packet.Packet;
import kaap.veiko.debuggerforker.packet.PacketSource;
import kaap.veiko.debuggerforker.packet.PacketStream;

public abstract class PacketBase implements Packet {

  private final int id;
  private final byte[] data;
  private final PacketSource source;

  public PacketBase(int id, byte[] data, PacketSource source) {
    this.id = id;
    this.data = data;
    this.source = source;
  }

  @Override
  public int getLength() {
    return Packet.HEADER_LENGTH + getData().length;
  }

  @Override
  public int getId() {
    return id;
  }

  @Override
  public short getFlags() {
    return (short) (isReply() ? -128 : 0);
  }

  @Override
  public byte[] getData() {
    return data;
  }

  @Override
  public PacketSource getSource() {
    return source;
  }

  @Override
  public boolean isSynthetic() {
    return false;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PacketBase)) {
      return false;
    }
    PacketBase that = (PacketBase) o;
    return getId() == that.getId() &&
        isReply() == that.isReply() &&
        Arrays.equals(getData(), that.getData()) &&
        Objects.equals(getSource(), that.getSource());
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(getId(), isReply(), getSource());
    result = 31 * result + Arrays.hashCode(getData());
    return result;
  }
}
