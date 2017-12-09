package kaap.veiko.debuggerforker.packet.internal;

import java.util.Arrays;
import java.util.Objects;

import kaap.veiko.debuggerforker.packet.Packet;
import kaap.veiko.debuggerforker.packet.PacketStream;

public abstract class PacketBase implements Packet {

  private final int length;
  private final int id;
  private final short flags;
  private final byte[] data;
  private final PacketStream source;

  public PacketBase(int length, int id, short flags, byte[] data, PacketStream source) {
    this.length = length;
    this.id = id;
    this.flags = flags;
    this.data = data;
    this.source = source;
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
  public byte[] getDataBytes() {
    return data;
  }

  @Override
  public PacketStream getSource() {
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
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PacketBase that = (PacketBase) o;
    return length == that.length &&
        id == that.id &&
        flags == that.flags &&
        Arrays.equals(data, that.data) &&
        Objects.equals(source, that.source);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(length, id, flags, source);
    result = 31 * result + Arrays.hashCode(data);
    return result;
  }
}
