package kaap.veiko.debuggerforker.commands;

import kaap.veiko.debuggerforker.packet.Packet;
import kaap.veiko.debuggerforker.types.VMInformation;

public class PacketCommand implements Command {

  private final Packet packet;
  private final int commandId;
  private final int commandSetId;
  private final boolean reply;

  public PacketCommand(Packet packet, int commandSetId, int commandId, boolean reply) {
    this.packet = packet;
    this.commandSetId = commandSetId;
    this.commandId = commandId;
    this.reply = reply;
  }

  @Override
  public int getCommandSetId() {
    return commandSetId;
  }

  @Override
  public int getCommandId() {
    return commandId;
  }

  @Override
  public boolean isReply() {
    return reply;
  }

  @Override
  public Packet asPacket(int id, VMInformation vmInformation) {
    return packet;
  }

  @Override
  public <T> T visit(CommandVisitor<T> visitor) {
    return null;
  }

  @Override
  public Packet getPacket() {
    return packet;
  }
}
