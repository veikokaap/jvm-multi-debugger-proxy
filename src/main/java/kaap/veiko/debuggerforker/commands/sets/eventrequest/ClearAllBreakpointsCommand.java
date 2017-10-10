package kaap.veiko.debuggerforker.commands.sets.eventrequest;

import kaap.veiko.debuggerforker.commands.Command;
import kaap.veiko.debuggerforker.commands.parser.annotations.JdwpCommand;
import kaap.veiko.debuggerforker.commands.parser.annotations.JdwpCommandConstructor;
import kaap.veiko.debuggerforker.commands.sets.CommandIdentifier;

@JdwpCommand(CommandIdentifier.CLEAR_ALL_BREAKPOINTS_COMMAND)
public class ClearAllBreakpointsCommand implements Command {
  @JdwpCommandConstructor
  public ClearAllBreakpointsCommand() {
  }

  @Override
  public String toString() {
    return "ClearAllBreakpointsCommand{}";
  }
}
