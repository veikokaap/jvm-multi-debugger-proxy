package kaap.veiko.debuggerforker.commands.commandsets.virtualmachine;

import kaap.veiko.debuggerforker.commands.CommandBase;
import kaap.veiko.debuggerforker.commands.CommandVisitor;
import kaap.veiko.debuggerforker.commands.MutableCommandPacket;
import kaap.veiko.debuggerforker.commands.parser.CommandDataReader;
import kaap.veiko.debuggerforker.commands.commandsets.CommandIdentifier;
import kaap.veiko.debuggerforker.packet.CommandPacket;
import kaap.veiko.debuggerforker.types.DataWriter;

public class DisposeCommand extends CommandBase<CommandPacket> {
  private static final CommandIdentifier COMMAND_IDENTIFIER = CommandIdentifier.DISPOSE_COMMAND;

  public static DisposeCommand create(int packetId) {
    MutableCommandPacket packet = MutableCommandPacket.create(packetId, COMMAND_IDENTIFIER);
    return new DisposeCommand(packet);
  }

  public static DisposeCommand read(CommandDataReader reader) {
    return new DisposeCommand((CommandPacket) reader.getPacket());
  }

  private DisposeCommand(CommandPacket packet) {
    super(packet, COMMAND_IDENTIFIER);
  }

  @Override
  public void writeCommand(DataWriter writer) {
  }

  @Override
  public void visit(CommandVisitor visitor) {
    visitor.visit(this);
  }

  @Override
  public String toString() {
    return "DisposeCommand{}";
  }
}
