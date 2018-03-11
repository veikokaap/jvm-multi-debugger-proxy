package kaap.veiko.debuggerforker.handlers;

import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaap.veiko.debuggerforker.ProxyCommandStream;
import kaap.veiko.debuggerforker.commands.Command;
import kaap.veiko.debuggerforker.commands.CommandVisitor;
import kaap.veiko.debuggerforker.commands.UnknownCommand;
import kaap.veiko.debuggerforker.commands.commandsets.event.CompositeEventCommand;
import kaap.veiko.debuggerforker.commands.commandsets.eventrequest.ClearAllBreakpointsCommand;
import kaap.veiko.debuggerforker.commands.commandsets.eventrequest.ClearEventRequestCommand;
import kaap.veiko.debuggerforker.commands.commandsets.eventrequest.ClearEventRequestReply;
import kaap.veiko.debuggerforker.commands.commandsets.eventrequest.SetEventRequestCommand;
import kaap.veiko.debuggerforker.commands.commandsets.eventrequest.SetEventRequestReply;
import kaap.veiko.debuggerforker.commands.commandsets.virtualmachine.DisposeCommand;
import kaap.veiko.debuggerforker.commands.commandsets.virtualmachine.DisposeReply;
import kaap.veiko.debuggerforker.commands.commandsets.virtualmachine.HoldEventsCommand;
import kaap.veiko.debuggerforker.commands.commandsets.virtualmachine.IdSizesReply;
import kaap.veiko.debuggerforker.commands.commandsets.virtualmachine.ReleaseEventsCommand;
import kaap.veiko.debuggerforker.commands.commandsets.virtualmachine.ResumeCommand;
import kaap.veiko.debuggerforker.packet.PacketSource;
import kaap.veiko.debuggerforker.packet.ReplyPacket;
import kaap.veiko.debuggerforker.types.VMInformation;

public class CommandHandler implements CommandVisitor {

  private static final Logger log = LoggerFactory.getLogger(CommandHandler.class);

  private final VMInformation vmInformation;
  private final ProxyCommandStream proxyCommandStream;
  private final RequestHandler requestHandler;

  public CommandHandler(VMInformation vmInformation, ProxyCommandStream proxyCommandStream) {
    this.vmInformation = vmInformation;
    this.proxyCommandStream = proxyCommandStream;
    requestHandler = new RequestHandler(this.vmInformation, this.proxyCommandStream);
  }

  @Override
  public void visit(CompositeEventCommand command) {
    requestHandler.handleCompositeEvent(command);
  }

  @Override
  public void visit(ClearAllBreakpointsCommand command) {
    requestHandler.handleClearAllBreakpointsCommand(command);
  }

  @Override
  public void visit(ClearEventRequestCommand command) {
    requestHandler.handleClearEventCommand(command);
  }

  @Override
  public void visit(ClearEventRequestReply clearEventRequestReply) {
    defaultHandle(clearEventRequestReply);
  }

  @Override
  public void visit(SetEventRequestCommand command) {
    requestHandler.handleSetEventCommand(command);
  }

  @Override
  public void visit(SetEventRequestReply reply) {
    requestHandler.handleSetEventReply(reply);
  }

  /**
   * When disconnecting, debugger sends a DisposeCommand. The VM must not receive this command, otherwise it will terminate the connection.
   * So block that command and also send a reply command back to the debugger so it would think the connection has been successfully terminated.
   * After that close the connection to the debugger.
   */
  @Override
  public void visit(DisposeCommand command) {
    PacketSource debugger = command.getSource();

    int id = command.getCommandId();
    proxyCommandStream.write(debugger, DisposeReply.create(id));
    proxyCommandStream.markForClosingAfterAllPacketsWritten(debugger);
  }

  /**
   * The DisposeCommand should never reach the VM so the VM should never send a reply.
   */
  @Override
  public void visit(DisposeReply reply) {
    log.warn("Received DisposeReply from VM!");
  }

  /**
   * Read IDSizes for knowing how to parse commands.
   */
  @Override
  public void visit(IdSizesReply reply) {
    vmInformation.setIdSizes(reply.getIdSizes());
    sendReplyToOriginalSource(reply);
  }

  @Override
  public void visit(HoldEventsCommand command) {
    command.getSource().setHoldEvents(true);
  }

  @Override
  public void visit(ReleaseEventsCommand command) {
    command.getSource().setHoldEvents(false);
  }

  @Override
  public void visit(ResumeCommand resumeCommand) {
    defaultHandle(resumeCommand);
  }

  @Override
  public void visit(UnknownCommand command) {
    defaultHandle(command);
  }

  private void defaultHandle(Command command) {
    if (command.getPacket().isReply()) {
      sendReplyToOriginalSource(command);
    }
    else {
      if (command.getSource().isDebugger()) {
        proxyCommandStream.writeToVm(command);
      }
      else {
        /* Biggest point of failure - if vm sends a command that we don't know, send it to all debuggers */
        proxyCommandStream.writeToAllDebuggers(command);
      }
    }
  }

  private void sendReplyToOriginalSource(Command<ReplyPacket> reply) {
    PacketSource originalSource = reply.getPacket().getCommandPacket().getSource();
    if (originalSource != null) {
      proxyCommandStream.write(originalSource, reply);
    }
  }
}
