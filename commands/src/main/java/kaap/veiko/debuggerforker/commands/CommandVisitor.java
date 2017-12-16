package kaap.veiko.debuggerforker.commands;

import kaap.veiko.debuggerforker.commands.sets.event.CompositeEventCommand;
import kaap.veiko.debuggerforker.commands.sets.eventrequest.ClearAllBreakpointsCommand;
import kaap.veiko.debuggerforker.commands.sets.eventrequest.ClearEventRequestCommand;
import kaap.veiko.debuggerforker.commands.sets.eventrequest.SetEventRequestCommand;
import kaap.veiko.debuggerforker.commands.sets.eventrequest.SetEventRequestReply;
import kaap.veiko.debuggerforker.commands.sets.virtualmachine.DisposeCommand;
import kaap.veiko.debuggerforker.commands.sets.virtualmachine.DisposeReply;
import kaap.veiko.debuggerforker.commands.sets.virtualmachine.IdSizesReplyCommand;

public interface CommandVisitor<T> {
  T visit(CompositeEventCommand command);

  T visit(ClearAllBreakpointsCommand command);

  T visit(ClearEventRequestCommand command);

  T visit(SetEventRequestCommand command);

  T visit(SetEventRequestReply command);

  T visit(DisposeCommand command);

  T visit(IdSizesReplyCommand command);

  T visit(DisposeReply disposeReply);

  T visit(UnknownCommand packetCommand);
}
