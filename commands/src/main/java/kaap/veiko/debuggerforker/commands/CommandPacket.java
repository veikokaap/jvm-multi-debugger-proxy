package kaap.veiko.debuggerforker.commands;

import java.util.Objects;

import kaap.veiko.debuggerforker.packet.Packet;
import kaap.veiko.debuggerforker.packet.PacketSource;

public class CommandPacket implements Packet {
  private final Packet packet;
  private final Command command;
  private final PacketSource source;

  public CommandPacket(Packet packet, Command command, PacketSource source) {
    this.packet = packet;
    this.command = command;
    this.source = source;
  }

  public Command getCommand() {
    return command;
  }

  @Override
  public int getLength() {
    return packet.getLength();
  }

  @Override
  public void setLength(int length) {
    packet.setLength(length);
  }

  @Override
  public int getId() {
    return packet.getId();
  }

  @Override
  public void setId(int id) {
    packet.setId(id);
  }

  @Override
  public short getFlags() {
    return packet.getFlags();
  }

  @Override
  public void setFlags(short flags) {
    packet.setFlags(flags);
  }

  @Override
  public short getCommandSetId() {
    return packet.getCommandSetId();
  }

  @Override
  public void setCommandSetId(short commandSetId) {
    packet.setCommandSetId(commandSetId);
  }

  @Override
  public short getCommandId() {
    return packet.getCommandId();
  }

  @Override
  public void setCommandId(short commandId) {
    packet.setCommandId(commandId);
  }

  @Override
  public short getErrorCode() {
    return packet.getErrorCode();
  }

  @Override
  public void setErrorCode(short errorCode) {
    packet.setErrorCode(errorCode);
  }

  @Override
  public byte[] getDataBytes() {
    return packet.getDataBytes();
  }

  @Override
  public void setDataBytes(byte[] dataBytes) {
    packet.setDataBytes(dataBytes);
  }

  @Override
  public boolean isReply() {
    return packet.isReply();
  }

  @Override
  public boolean hasData() {
    return packet.hasData();
  }

  public PacketSource getSource() {
    return source;
  }

  @Override
  public String toString() {
    return "CommandPacket{" +
        "packet=" + packet +
        ", command=" + command +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CommandPacket that = (CommandPacket) o;
    return Objects.equals(packet, that.packet);
  }

  @Override
  public int hashCode() {

    return Objects.hash(packet);
  }
}
