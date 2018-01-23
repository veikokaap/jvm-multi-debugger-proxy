package kaap.veiko.debuggerforker;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaap.veiko.debuggerforker.commands.CommandStream;
import kaap.veiko.debuggerforker.commands.CommandVisitor;
import kaap.veiko.debuggerforker.commands.UnknownCommand;
import kaap.veiko.debuggerforker.commands.sets.event.CompositeEventCommand;
import kaap.veiko.debuggerforker.commands.sets.eventrequest.ClearAllBreakpointsCommand;
import kaap.veiko.debuggerforker.commands.sets.eventrequest.ClearEventRequestCommand;
import kaap.veiko.debuggerforker.commands.sets.eventrequest.SetEventRequestCommand;
import kaap.veiko.debuggerforker.commands.sets.eventrequest.SetEventRequestReply;
import kaap.veiko.debuggerforker.commands.sets.virtualmachine.DisposeCommand;
import kaap.veiko.debuggerforker.commands.sets.virtualmachine.DisposeReply;
import kaap.veiko.debuggerforker.commands.sets.virtualmachine.IdSizesReply;
import kaap.veiko.debuggerforker.packet.PacketStream;
import kaap.veiko.debuggerforker.types.VMInformation;

public class CommandHandler implements CommandVisitor<CommandResult> {

  private static final Logger log = LoggerFactory.getLogger(CommandHandler.class);

  private final VMInformation vmInformation;
  private final ProxyCommandStream proxyCommandStream;

  public CommandHandler(VMInformation vmInformation, ProxyCommandStream proxyCommandStream) {
    this.vmInformation = vmInformation;
    this.proxyCommandStream = proxyCommandStream;
  }

  @Override
  public CommandResult visit(CompositeEventCommand command) {
    return CommandResult.forwardPacket(command);
  }

  @Override
  public CommandResult visit(ClearAllBreakpointsCommand command) {
    return CommandResult.forwardPacket(command);
  }

  @Override
  public CommandResult visit(ClearEventRequestCommand command) {
    return CommandResult.forwardPacket(command);
  }

  @Override
  public CommandResult visit(SetEventRequestCommand command) {
    return CommandResult.forwardPacket(command);
  }

  @Override
  public CommandResult visit(SetEventRequestReply command) {
    return CommandResult.forwardPacket(command);
  }

  /*
  When disconnecting, debugger sends a DisposeCommand. The VM must not receive this command, so block that command.
  Also send a reply command back to the debugger so it would think the connection has been successfully terminated.
  After that close the connection to the debugger.
   */
  @Override
  public CommandResult visit(DisposeCommand command) {
    PacketStream stream = command.getPacket().getSource();

    int id = command.getCommandId();
    DisposeReply disposeReply = DisposeReply.create(id);

    try {
      stream.write(disposeReply.getPacket());
    } catch (IOException error) {
      log.error("Failed to build packet from command {}", disposeReply, error);
    } finally {
      stream.close();
    }

    return CommandResult.NO_PACKETS_SENT;
  }

  /*
  Read IDSizes for knowing how to parse commands.
   */
  @Override
  public CommandResult visit(IdSizesReply command) {
    vmInformation.setIdSizes(command.getIdSizes());
    return CommandResult.forwardPacket(command);
  }

  @Override
  public CommandResult visit(DisposeReply command) {
    return CommandResult.NO_PACKETS_SENT;
  }

  @Override
  public CommandResult visit(UnknownCommand command) {
    return CommandResult.forwardPacket(command);
  }
}
