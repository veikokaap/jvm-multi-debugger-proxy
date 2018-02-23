package kaap.veiko.debuggerforker.commands.commandsets.eventrequest;

import kaap.veiko.debuggerforker.commands.CommandBase;
import kaap.veiko.debuggerforker.commands.CommandVisitor;
import kaap.veiko.debuggerforker.commands.MutableCommandPacket;
import kaap.veiko.debuggerforker.commands.parser.CommandDataReader;
import kaap.veiko.debuggerforker.commands.parser.CommandDataWriter;
import kaap.veiko.debuggerforker.commands.commandsets.CommandIdentifier;
import kaap.veiko.debuggerforker.packet.Packet;

public class ClearAllBreakpointsCommand extends CommandBase {
  private static final CommandIdentifier COMMAND_IDENTIFIER = CommandIdentifier.CLEAR_ALL_BREAKPOINTS_COMMAND;

  public static ClearAllBreakpointsCommand create(int packetId) {
    MutableCommandPacket packet = MutableCommandPacket.create(packetId, COMMAND_IDENTIFIER);
    return new ClearAllBreakpointsCommand(packet);
  }

  public static ClearAllBreakpointsCommand read(CommandDataReader reader) {
    return new ClearAllBreakpointsCommand(reader.getPacket());
  }

  private ClearAllBreakpointsCommand(Packet packet) {
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
    return "ClearAllBreakpointsCommand{}";
  }
}
