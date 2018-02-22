package kaap.veiko.debuggerforker.commands;

import kaap.veiko.debuggerforker.commands.sets.event.CompositeEventCommand;
import kaap.veiko.debuggerforker.commands.sets.eventrequest.ClearAllBreakpointsCommand;
import kaap.veiko.debuggerforker.commands.sets.eventrequest.ClearEventRequestCommand;
import kaap.veiko.debuggerforker.commands.sets.eventrequest.SetEventRequestCommand;
import kaap.veiko.debuggerforker.commands.sets.eventrequest.SetEventRequestReply;
import kaap.veiko.debuggerforker.commands.sets.virtualmachine.DisposeCommand;
import kaap.veiko.debuggerforker.commands.sets.virtualmachine.DisposeReply;
import kaap.veiko.debuggerforker.commands.sets.virtualmachine.IdSizesReply;

public interface CommandVisitor {
  void visit(CompositeEventCommand command);

  void visit(ClearAllBreakpointsCommand command);

  void visit(ClearEventRequestCommand command);

  void visit(SetEventRequestCommand command);

  void visit(SetEventRequestReply command);

  void visit(DisposeCommand command);

  void visit(IdSizesReply command);

  void visit(DisposeReply disposeReply);

  void visit(UnknownCommand packetCommand);
}
