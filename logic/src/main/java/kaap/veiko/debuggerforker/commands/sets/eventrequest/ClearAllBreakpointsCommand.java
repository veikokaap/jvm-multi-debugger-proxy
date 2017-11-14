package kaap.veiko.debuggerforker.commands.sets.eventrequest;

import java.util.Collections;
import java.util.List;

import kaap.veiko.debuggerforker.commands.CommandBase;
import kaap.veiko.debuggerforker.commands.parser.annotations.JdwpCommand;
import kaap.veiko.debuggerforker.commands.parser.annotations.JdwpCommandConstructor;
import kaap.veiko.debuggerforker.commands.sets.CommandIdentifier;

@JdwpCommand(CommandIdentifier.CLEAR_ALL_BREAKPOINTS_COMMAND)
public class ClearAllBreakpointsCommand extends CommandBase {
  @JdwpCommandConstructor
  public ClearAllBreakpointsCommand() {
  }

  @Override
  public List<Object> getPacketValues() {
    return Collections.emptyList();
  }

  @Override
  public String toString() {
    return "ClearAllBreakpointsCommand{}";
  }
}
