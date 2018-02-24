package kaap.veiko.debuggerforker.commands;

import kaap.veiko.debuggerforker.commands.commandsets.event.CompositeEventCommand;
import kaap.veiko.debuggerforker.commands.commandsets.eventrequest.ClearAllBreakpointsCommand;
import kaap.veiko.debuggerforker.commands.commandsets.eventrequest.ClearEventRequestCommand;
import kaap.veiko.debuggerforker.commands.commandsets.eventrequest.SetEventRequestCommand;
import kaap.veiko.debuggerforker.commands.commandsets.eventrequest.SetEventRequestReply;
import kaap.veiko.debuggerforker.commands.commandsets.virtualmachine.DisposeCommand;
import kaap.veiko.debuggerforker.commands.commandsets.virtualmachine.DisposeReply;
import kaap.veiko.debuggerforker.commands.commandsets.virtualmachine.HoldEventsCommand;
import kaap.veiko.debuggerforker.commands.commandsets.virtualmachine.IdSizesReply;
import kaap.veiko.debuggerforker.commands.commandsets.virtualmachine.ReleaseEventsCommand;

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

  void visit(HoldEventsCommand holdEventsCommand);

  void visit(ReleaseEventsCommand releaseEventsCommand);
}
