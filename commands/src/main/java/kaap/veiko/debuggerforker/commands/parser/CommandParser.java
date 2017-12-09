package kaap.veiko.debuggerforker.commands.parser;

import java.nio.ByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaap.veiko.debuggerforker.commands.Command;
import kaap.veiko.debuggerforker.commands.PacketCommand;
import kaap.veiko.debuggerforker.commands.sets.CommandIdentifier;
import kaap.veiko.debuggerforker.packet.CommandPacket;
import kaap.veiko.debuggerforker.packet.Packet;
import kaap.veiko.debuggerforker.packet.ReplyPacket;
import kaap.veiko.debuggerforker.types.VMInformation;

public class CommandParser {
  private final static Logger log = LoggerFactory.getLogger(CommandParser.class);

  private final VMInformation vmInformation;

  public CommandParser(VMInformation vmInformation) {
    this.vmInformation = vmInformation;
  }

  public Command parse(Packet packet) {
    CommandPacket commandPacket;
    if (packet.isReply()) {
      commandPacket = ((ReplyPacket) packet).getCommandPacket();
    } else {
      commandPacket = (CommandPacket) packet;
    }

    CommandDataReader commandDataReader = new CommandDataReader(ByteBuffer.wrap(packet.getDataBytes()), vmInformation);
    try {
      CommandIdentifier identifier = CommandIdentifier.of(commandPacket.getCommandSetId(), commandPacket.getCommandId(), packet.isReply());
      return commandDataReader.readCommand(identifier, packet);
    }
    catch (Exception e) {
      log.error("Failed to find CommandIdentifier", e);
      return new PacketCommand(packet, commandPacket.getCommandSetId(), commandPacket.getCommandId(), packet.isReply());
    }
  }
}
