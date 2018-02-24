package kaap.veiko.debuggerforker.commands.commandsets.eventrequest;

import kaap.veiko.debuggerforker.commands.CommandBase;
import kaap.veiko.debuggerforker.commands.CommandVisitor;
import kaap.veiko.debuggerforker.commands.MutableCommandPacket;
import kaap.veiko.debuggerforker.commands.parser.CommandDataReader;
import kaap.veiko.debuggerforker.commands.commandsets.CommandIdentifier;
import kaap.veiko.debuggerforker.packet.CommandPacket;
import kaap.veiko.debuggerforker.types.DataWriter;

public class ClearAllBreakpointsCommand extends CommandBase<CommandPacket> {
  private static final CommandIdentifier COMMAND_IDENTIFIER = CommandIdentifier.CLEAR_ALL_BREAKPOINTS_COMMAND;

  public static ClearAllBreakpointsCommand create(int packetId) {
    MutableCommandPacket packet = MutableCommandPacket.create(packetId, COMMAND_IDENTIFIER);
    return new ClearAllBreakpointsCommand(packet);
  }

  public static ClearAllBreakpointsCommand read(CommandDataReader reader) {
    return new ClearAllBreakpointsCommand((CommandPacket) reader.getPacket());
  }

  private ClearAllBreakpointsCommand(CommandPacket packet) {
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
    return "ClearAllBreakpointsCommand{}";
  }
}
