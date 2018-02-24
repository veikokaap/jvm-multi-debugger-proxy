package kaap.veiko.debuggerforker.commands.commandsets.virtualmachine;

import kaap.veiko.debuggerforker.commands.CommandBase;
import kaap.veiko.debuggerforker.commands.CommandVisitor;
import kaap.veiko.debuggerforker.commands.MutableCommandPacket;
import kaap.veiko.debuggerforker.commands.commandsets.CommandIdentifier;
import kaap.veiko.debuggerforker.commands.parser.CommandDataReader;
import kaap.veiko.debuggerforker.commands.parser.CommandDataWriter;
import kaap.veiko.debuggerforker.packet.CommandPacket;

public class ReleaseEventsCommand extends CommandBase<CommandPacket> {
  private static final CommandIdentifier COMMAND_IDENTIFIER = CommandIdentifier.RELEASE_EVENTS_COMMAND;

  public static ReleaseEventsCommand create(int packetId) {
    MutableCommandPacket packet = MutableCommandPacket.create(packetId, COMMAND_IDENTIFIER);
    return new ReleaseEventsCommand(packet);
  }

  public static ReleaseEventsCommand read(CommandDataReader reader) {
    return new ReleaseEventsCommand((CommandPacket) reader.getPacket());
  }

  private ReleaseEventsCommand(CommandPacket packet) {
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
    return "ReleaseEventsCommand{}";
  }
}
