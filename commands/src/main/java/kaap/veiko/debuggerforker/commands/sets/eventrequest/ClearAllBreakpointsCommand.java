package kaap.veiko.debuggerforker.commands.sets.eventrequest;


import kaap.veiko.debuggerforker.commands.CommandBase;
import kaap.veiko.debuggerforker.commands.CommandVisitor;
import kaap.veiko.debuggerforker.commands.parser.CommandDataWriter;
import kaap.veiko.debuggerforker.commands.sets.CommandIdentifier;
import kaap.veiko.debuggerforker.packet.Packet;
import kaap.veiko.debuggerforker.types.DataReader;

public class ClearAllBreakpointsCommand extends CommandBase {
  public static final CommandIdentifier COMMAND_IDENTIFIER = CommandIdentifier.CLEAR_ALL_BREAKPOINTS_COMMAND;

  public ClearAllBreakpointsCommand(DataReader reader, Packet packet) {
    super();
    setPacket(packet);
  }

  @Override
  public void writeCommand(CommandDataWriter writer) {
  }

  @Override
  protected CommandIdentifier getCommandIdentifier() {
    return COMMAND_IDENTIFIER;
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
