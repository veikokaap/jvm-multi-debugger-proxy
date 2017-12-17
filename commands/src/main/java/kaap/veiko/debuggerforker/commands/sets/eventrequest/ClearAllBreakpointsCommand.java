package kaap.veiko.debuggerforker.commands.sets.eventrequest;


import kaap.veiko.debuggerforker.commands.CommandBase;
import kaap.veiko.debuggerforker.commands.CommandVisitor;
import kaap.veiko.debuggerforker.commands.SyntheticPacket;
import kaap.veiko.debuggerforker.commands.parser.CommandDataReader;
import kaap.veiko.debuggerforker.commands.parser.CommandDataWriter;
import kaap.veiko.debuggerforker.commands.sets.CommandIdentifier;
import kaap.veiko.debuggerforker.packet.Packet;

public class ClearAllBreakpointsCommand extends CommandBase {
  private static final CommandIdentifier COMMAND_IDENTIFIER = CommandIdentifier.CLEAR_ALL_BREAKPOINTS_COMMAND;

  public static ClearAllBreakpointsCommand create(int packetId) {
    SyntheticPacket packet = SyntheticPacket.create(packetId, COMMAND_IDENTIFIER);
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
  public <T> T visit(CommandVisitor<T> visitor) {
    return visitor.visit(this);
  }

  @Override
  public String toString() {
    return "ClearAllBreakpointsCommand{}";
  }
}
