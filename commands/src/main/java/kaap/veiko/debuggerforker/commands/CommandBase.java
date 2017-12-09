package kaap.veiko.debuggerforker.commands;

import kaap.veiko.debuggerforker.commands.parser.CommandType;
import kaap.veiko.debuggerforker.commands.sets.CommandIdentifier;
import kaap.veiko.debuggerforker.packet.Packet;

public abstract class CommandBase implements Command {

  private final Packet packet;

  protected CommandBase(Packet packet) {
    this.packet = packet;
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

  protected abstract CommandIdentifier getCommandIdentifier();
}
