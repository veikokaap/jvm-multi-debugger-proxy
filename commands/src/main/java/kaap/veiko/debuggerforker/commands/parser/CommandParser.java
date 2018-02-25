package kaap.veiko.debuggerforker.commands.parser;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaap.veiko.debuggerforker.commands.Command;
import kaap.veiko.debuggerforker.commands.UnknownCommand;
import kaap.veiko.debuggerforker.commands.commandsets.CommandIdentifier;
import kaap.veiko.debuggerforker.packet.CommandPacket;
import kaap.veiko.debuggerforker.packet.Packet;
import kaap.veiko.debuggerforker.packet.PacketVisitor;
import kaap.veiko.debuggerforker.packet.ReplyPacket;
import kaap.veiko.debuggerforker.types.VMInformation;

public class CommandParser {
  private final static Logger log = LoggerFactory.getLogger(CommandParser.class);

  private final VMInformation vmInformation;
  private final CommandPacketFinder commandPacketFinder = new CommandPacketFinder();

  public CommandParser(VMInformation vmInformation) {
    this.vmInformation = vmInformation;
  }

  public Command parse(Packet packet) {
    CommandPacket commandPacket = packet.visit(commandPacketFinder);

    CommandDataReader commandDataReader = new CommandDataReader(packet, vmInformation);
    try {
      CommandIdentifier identifier = CommandIdentifier.of(commandPacket.getCommandSetId(), commandPacket.getCommandId(), packet.isReply());
      return commandDataReader.readCommand(identifier, packet);
    }
    catch (IOException e) {
      log.warn(e.getMessage());
      return new UnknownCommand(packet, commandPacket.getCommandSetId(), commandPacket.getCommandId(), packet.isReply());
    }
  }

  private class CommandPacketFinder implements PacketVisitor<CommandPacket> {
    @Override
    public CommandPacket visit(ReplyPacket replyPacket) {
      return replyPacket.getCommandPacket();
    }

    @Override
    public CommandPacket visit(CommandPacket commandPacket) {
      return commandPacket;
    }
  }
}
