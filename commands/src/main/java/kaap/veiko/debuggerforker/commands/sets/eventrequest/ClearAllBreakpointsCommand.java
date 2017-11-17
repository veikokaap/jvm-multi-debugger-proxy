package kaap.veiko.debuggerforker.commands.sets.eventrequest;


import kaap.veiko.debuggerforker.commands.CommandBase;
import kaap.veiko.debuggerforker.commands.parser.CommandDataWriter;
import kaap.veiko.debuggerforker.commands.sets.CommandIdentifier;
import kaap.veiko.debuggerforker.types.DataReader;

public class ClearAllBreakpointsCommand extends CommandBase {
  public static final CommandIdentifier COMMAND_IDENTIFIER = CommandIdentifier.CLEAR_ALL_BREAKPOINTS_COMMAND;

  public ClearAllBreakpointsCommand(DataReader reader) {
  }

  @Override
  protected void writeCommand(CommandDataWriter writer) {
  }

  @Override
  protected CommandIdentifier getCommandIdentifier() {
    return COMMAND_IDENTIFIER;
  }

  @Override
  public String toString() {
    return "ClearAllBreakpointsCommand{}";
  }
}
