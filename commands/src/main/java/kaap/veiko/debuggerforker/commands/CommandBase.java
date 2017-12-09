package kaap.veiko.debuggerforker.commands;

import kaap.veiko.debuggerforker.commands.parser.CommandType;
import kaap.veiko.debuggerforker.commands.sets.CommandIdentifier;
import kaap.veiko.debuggerforker.packet.Packet;

public abstract class CommandBase implements Command {

  private Packet packet;

  protected CommandBase() {
  }

  @Override
  public Packet getPacket() {
    return packet;
  }

  @Override
  public int getCommandSetId() {
    return getCommandIdentifier().getCommandSetId();
  }

  @Override
  public int getCommandId() {
    return getCommandIdentifier().getCommandId();
  }

  @Override
  public boolean isReply() {
    return getCommandIdentifier().getType() == CommandType.REPLY;
  }

  protected void setPacket(Packet packet) {
    this.packet = packet;
  }

  protected void createSyntheticPacket(PacketBuilder packetBuilder) {
    this.packet = packetBuilder.build(this);
  }

  protected abstract CommandIdentifier getCommandIdentifier();
}
