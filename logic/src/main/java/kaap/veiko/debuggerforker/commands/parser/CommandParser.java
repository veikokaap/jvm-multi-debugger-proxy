package kaap.veiko.debuggerforker.commands.parser;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaap.veiko.debuggerforker.types.VMInformation;
import kaap.veiko.debuggerforker.commands.Command;
import kaap.veiko.debuggerforker.packet.Packet;

public class CommandParser {

  private final static Logger log = LoggerFactory.getLogger(CommandParser.class);

  private final ConstructorFinder constructorFinder = new ConstructorFinder();
  private final CommandClassFinder commandClassFinder = new CommandClassFinder();
  private final CommandInstantiator commandInstantiator;

  public CommandParser(VMInformation vmInformation) {
    commandInstantiator = new CommandInstantiator(new ParameterParser(vmInformation));
  }

  public Command parse(Packet packet) {
    Optional<? extends Command> command = Optional.of(packet)
        .map(commandClassFinder::find)
        .map(constructorFinder::find)
        .map(constructor -> commandInstantiator.newInstanceFromBytes(constructor, packet.getDataBytes()));

    if (!command.isPresent()) {
      log.warn("Failed to parse the command from packet '{}'", packet);
      return null;
    }

    return command.get();
  }
}
