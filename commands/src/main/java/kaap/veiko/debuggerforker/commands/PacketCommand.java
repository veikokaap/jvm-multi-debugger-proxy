package kaap.veiko.debuggerforker.commands;

import kaap.veiko.debuggerforker.commands.parser.CommandDataWriter;
import kaap.veiko.debuggerforker.packet.Packet;

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
  public <T> T visit(CommandVisitor<T> visitor) {
    return null;
  }

  @Override
  public Packet getPacket() {
    return packet;
  }

  @Override
  public void writeCommand(CommandDataWriter writer) {
    for (byte b : packet.getDataBytes()) {
      writer.writeByte(b);
    }
  }
}
