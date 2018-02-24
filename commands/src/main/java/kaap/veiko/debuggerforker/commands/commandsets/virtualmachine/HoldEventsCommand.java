package kaap.veiko.debuggerforker.commands.commandsets.virtualmachine;

import kaap.veiko.debuggerforker.commands.CommandBase;
import kaap.veiko.debuggerforker.commands.CommandVisitor;
import kaap.veiko.debuggerforker.commands.MutableCommandPacket;
import kaap.veiko.debuggerforker.commands.commandsets.CommandIdentifier;
import kaap.veiko.debuggerforker.commands.parser.CommandDataReader;
import kaap.veiko.debuggerforker.commands.parser.CommandDataWriter;
import kaap.veiko.debuggerforker.packet.CommandPacket;

public class HoldEventsCommand extends CommandBase<CommandPacket> {
  private static final CommandIdentifier COMMAND_IDENTIFIER = CommandIdentifier.HOLD_EVENTS_COMMAND;

  public static HoldEventsCommand create(int packetId) {
    MutableCommandPacket packet = MutableCommandPacket.create(packetId, COMMAND_IDENTIFIER);
    return new HoldEventsCommand(packet);
  }

  public static HoldEventsCommand read(CommandDataReader reader) {
    return new HoldEventsCommand((CommandPacket) reader.getPacket());
  }

  private HoldEventsCommand(CommandPacket packet) {
    super(packet, COMMAND_IDENTIFIER);
  }

  @Override
  public void writeCommand(CommandDataWriter writer) {
  }

  @Override
  public void visit(CommandVisitor visitor) {
    visitor.visit(this);
  }

  @Override
  public String toString() {
    return "HoldEventsCommand{}";
  }
}
