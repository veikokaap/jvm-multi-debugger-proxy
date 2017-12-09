package kaap.veiko.debuggerforker;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaap.veiko.debuggerforker.commands.CommandStream;
import kaap.veiko.debuggerforker.commands.CommandVisitor;
import kaap.veiko.debuggerforker.commands.PacketBuilder;
import kaap.veiko.debuggerforker.commands.PacketCommand;
import kaap.veiko.debuggerforker.commands.sets.event.CompositeEventCommand;
import kaap.veiko.debuggerforker.commands.sets.eventrequest.ClearAllBreakpointsCommand;
import kaap.veiko.debuggerforker.commands.sets.eventrequest.ClearEventRequestCommand;
import kaap.veiko.debuggerforker.commands.sets.eventrequest.SetEventRequestCommand;
import kaap.veiko.debuggerforker.commands.sets.eventrequest.SetEventRequestReply;
import kaap.veiko.debuggerforker.commands.sets.virtualmachine.DisposeCommand;
import kaap.veiko.debuggerforker.commands.sets.virtualmachine.DisposeReply;
import kaap.veiko.debuggerforker.commands.sets.virtualmachine.IdSizesReplyCommand;
import kaap.veiko.debuggerforker.packet.PacketStream;
import kaap.veiko.debuggerforker.types.VMInformation;

public class CommandHandler implements CommandVisitor<CommandResult> {

  private final VMInformation vmInformation;
  private final ProxyPacketStream debuggerStream;
  private final CommandStream vmStream;
  private final PacketBuilder packetBuilder;
  private final Logger log = LoggerFactory.getLogger(CommandHandler.class);

  public CommandHandler(VMInformation vmInformation, ProxyPacketStream debuggerStream, CommandStream vmStream) {
    this.vmInformation = vmInformation;
    this.debuggerStream = debuggerStream;
    this.vmStream = vmStream;
    this.packetBuilder = new PacketBuilder(vmInformation);
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

    DisposeReply disposeReply = new DisposeReply();
    int id = command.getCommandId();

    try {
      stream.write(packetBuilder.build(disposeReply, id));
    } catch (IOException error) {
      log.error("Failed to build packet from command {}", disposeReply, error);
    } finally {
      try {
        stream.close();
      }
      catch (IOException error) {
        log.error("Exception when trying to close stream.", error);
      }
    }

    return CommandResult.NO_PACKETS_SENT;
  }

  /*
  Read IDSizes for knowing how to parse commands.
   */
  @Override
  public CommandResult visit(IdSizesReplyCommand command) {
    vmInformation.setIdSizes(command.getIdSizes());
    return CommandResult.forwardPacket(command);
  }

  @Override
  public CommandResult visit(DisposeReply command) {
    return CommandResult.NO_PACKETS_SENT;
  }

  @Override
  public CommandResult visit(PacketCommand command) {
    return CommandResult.forwardPacket(command);
  }
}
