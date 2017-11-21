package kaap.veiko.debuggerforker.commands.parser;

import java.nio.ByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaap.veiko.debuggerforker.commands.Command;
import kaap.veiko.debuggerforker.commands.sets.CommandIdentifier;
import kaap.veiko.debuggerforker.packet.Packet;
import kaap.veiko.debuggerforker.types.VMInformation;

public class CommandParser {
  private final static Logger log = LoggerFactory.getLogger(CommandParser.class);

  private final VMInformation vmInformation;

  public CommandParser(VMInformation vmInformation) {
    this.vmInformation = vmInformation;
  }

  public Command parse(Packet packet) {
    CommandDataReader commandDataReader = new CommandDataReader(ByteBuffer.wrap(packet.getDataBytes()), vmInformation);
    CommandIdentifier identifier = null;
    try {
      identifier = CommandIdentifier.of(packet.getCommandSetId(), packet.getCommandId(), packet.isReply());
    }
    catch (Exception e) {
      log.error("Failed to find CommandIdentifier", e);
      return null;
    }

    return commandDataReader.readCommand(identifier);
  }
}
