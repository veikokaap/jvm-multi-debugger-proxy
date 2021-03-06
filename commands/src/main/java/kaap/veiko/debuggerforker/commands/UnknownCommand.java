package kaap.veiko.debuggerforker.commands;

import kaap.veiko.debuggerforker.packet.Packet;
import kaap.veiko.debuggerforker.packet.PacketSource;
import kaap.veiko.debuggerforker.types.DataWriter;

public class UnknownCommand implements Command {

  private final Packet packet;
  private final int commandId;
  private final int commandSetId;
  private final boolean reply;

  public UnknownCommand(Packet packet, int commandSetId, int commandId, boolean reply) {
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
  public void visit(CommandVisitor visitor) {
    visitor.visit(this);
  }

  @Override
  public Packet getPacket() {
    return packet;
  }

  @Override
  public PacketSource getSource() {
    return packet.getSource();
  }

  @Override
  public void writeCommand(DataWriter writer) {
    for (byte b : packet.getData()) {
      writer.writeByte(b);
    }
  }

  @Override
  public String toString() {
    return "UnknownCommand{" +
        "packet=" + packet +
        '}';
  }
}
