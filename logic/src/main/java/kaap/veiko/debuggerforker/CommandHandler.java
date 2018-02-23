package kaap.veiko.debuggerforker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaap.veiko.debuggerforker.commands.Command;
import kaap.veiko.debuggerforker.commands.CommandVisitor;
import kaap.veiko.debuggerforker.commands.UnknownCommand;
import kaap.veiko.debuggerforker.commands.commandsets.event.CompositeEventCommand;
import kaap.veiko.debuggerforker.commands.commandsets.eventrequest.ClearAllBreakpointsCommand;
import kaap.veiko.debuggerforker.commands.commandsets.eventrequest.ClearEventRequestCommand;
import kaap.veiko.debuggerforker.commands.commandsets.eventrequest.SetEventRequestCommand;
import kaap.veiko.debuggerforker.commands.commandsets.eventrequest.SetEventRequestReply;
import kaap.veiko.debuggerforker.commands.commandsets.virtualmachine.DisposeCommand;
import kaap.veiko.debuggerforker.commands.commandsets.virtualmachine.DisposeReply;
import kaap.veiko.debuggerforker.commands.commandsets.virtualmachine.IdSizesReply;
import kaap.veiko.debuggerforker.packet.PacketSource;
import kaap.veiko.debuggerforker.packet.ReplyPacket;
import kaap.veiko.debuggerforker.types.VMInformation;

public class CommandHandler implements CommandVisitor {

  private static final Logger log = LoggerFactory.getLogger(CommandHandler.class);

  private final VMInformation vmInformation;
  private final ProxyCommandStream proxyCommandStream;

  public CommandHandler(VMInformation vmInformation, ProxyCommandStream proxyCommandStream) {
    this.vmInformation = vmInformation;
    this.proxyCommandStream = proxyCommandStream;
  }

  @Override
  public void visit(CompositeEventCommand command) {
    defaultHandle(command);
  }

  @Override
  public void visit(ClearAllBreakpointsCommand command) {
    defaultHandle(command);
  }

  @Override
  public void visit(ClearEventRequestCommand command) {
    defaultHandle(command);
  }

  @Override
  public void visit(SetEventRequestCommand command) {
    defaultHandle(command);
  }

  @Override
  public void visit(SetEventRequestReply command) {
    sendReplyToOriginalSource(command);
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
   * This command should NEVER reach the vm, otherwise it will disconnect from the proxy.
   */
  @Override
  public void visit(DisposeReply reply) {
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
    proxyCommandStream.write(originalSource, reply);
  }
}
