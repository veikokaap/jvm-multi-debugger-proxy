package kaap.veiko.debuggerforker;

import kaap.veiko.debuggerforker.commands.CommandVisitor;
import kaap.veiko.debuggerforker.commands.sets.event.CompositeEventCommand;
import kaap.veiko.debuggerforker.commands.sets.eventrequest.ClearAllBreakpointsCommand;
import kaap.veiko.debuggerforker.commands.sets.eventrequest.ClearEventRequestCommand;
import kaap.veiko.debuggerforker.commands.sets.eventrequest.SetEventRequestCommand;
import kaap.veiko.debuggerforker.commands.sets.eventrequest.SetEventRequestReply;
import kaap.veiko.debuggerforker.commands.sets.virtualmachine.DisposeCommand;
import kaap.veiko.debuggerforker.commands.sets.virtualmachine.IdSizesReplyCommand;
import kaap.veiko.debuggerforker.types.VMInformation;

public class CommandHandler implements CommandVisitor<CommandResult> {

  private final VMInformation vmInformation;

  public CommandHandler(VMInformation vmInformation) {
    this.vmInformation = vmInformation;
  }

  @Override
  public CommandResult visit(CompositeEventCommand command) {

    return null;
  }

  @Override
  public CommandResult visit(ClearAllBreakpointsCommand command) {

    return null;
  }

  @Override
  public CommandResult visit(ClearEventRequestCommand command) {

    return null;
  }

  @Override
  public CommandResult visit(SetEventRequestCommand command) {

    return null;
  }

  @Override
  public CommandResult visit(SetEventRequestReply command) {

    return null;
  }

  @Override
  public CommandResult visit(DisposeCommand command) {

    return null;
  }

  @Override
  public CommandResult visit(IdSizesReplyCommand command) {
    vmInformation.setIdSizes(command.getIdSizes());
    return null;
  }
}
