package kaap.veiko.debuggerforker.commands;

import kaap.veiko.debuggerforker.commands.parser.CommandType;
import kaap.veiko.debuggerforker.commands.sets.CommandIdentifier;
import kaap.veiko.debuggerforker.packet.Packet;
import kaap.veiko.debuggerforker.packet.PacketSource;

public abstract class CommandBase<T extends Packet> implements Command<T> {

  private final T packet;
  private final CommandIdentifier commandIdentifier;

  protected CommandBase(T packet, CommandIdentifier commandIdentifier) {
    this.packet = packet;
    this.commandIdentifier = commandIdentifier;
  }

  @Override
  public T getPacket() {
    return packet;
  }

  @Override
  public PacketSource getSource() {
    return packet.getSource();
  }

  @Override
  public int getCommandSetId() {
    return commandIdentifier.getCommandSetId();
  }

  @Override
  public int getCommandId() {
    return commandIdentifier.getCommandId();
  }

  @Override
  public boolean isReply() {
    return commandIdentifier.getType() == CommandType.REPLY;
  }
}
