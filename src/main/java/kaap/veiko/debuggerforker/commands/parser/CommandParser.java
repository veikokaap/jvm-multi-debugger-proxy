package kaap.veiko.debuggerforker.commands.parser;

import java.lang.reflect.Constructor;
import java.nio.ByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaap.veiko.debuggerforker.VMInformation;
import kaap.veiko.debuggerforker.commands.Command;
import kaap.veiko.debuggerforker.packet.Packet;

public class CommandParser {

  private final static Logger log = LoggerFactory.getLogger(CommandParser.class);

  private final ConstructorFinder constructorFinder = new ConstructorFinder();
  private final CommandClassFinder commandClassFinder = new CommandClassFinder();
  private final VMInformation vmInformation;
  private final ParameterParser parameterParser;

  public CommandParser(VMInformation vmInformation) {
    this.vmInformation = vmInformation;
    this.parameterParser = new ParameterParser(vmInformation);
  }

  public Command parse(Packet packet) {
    Class<?> commandClass = commandClassFinder.find(packet.getCommandSet(), packet.getCommand(), packet.isReply());
    if (commandClass == null) {
      return null;
    }

    Constructor<?> constructor = constructorFinder.find(commandClass);
    if (constructor == null) {
      return null;
    }

    ByteBuffer dataBuffer = ByteBuffer.wrap(packet.getDataBytes());
    Object command = newInstanceFromByteBuffer(constructor, dataBuffer);

    if (command == null) {
      log.error("Parsed command is null. Command class '{}'. Constructor '{}'", commandClass, constructor);
      return null;
    }
    else if (!(command instanceof Command)) {
      log.error("'{}' isn't an instance of '{}'", command, Command.class.getName());
      return null;
    }

    return (Command) command;
  }

  private Object newInstanceFromByteBuffer(Constructor<?> constructor, ByteBuffer dataBuffer) {
    Object[] parameterValues;
    try {
      parameterValues = parameterParser.parseMultipleValuesFromBuffer(
          dataBuffer,
          constructor.getParameters()
      );
    }
    catch (ReflectiveOperationException e) {
      log.error("Exception while trying to parse values for a Command constructor '{}'.", constructor, e);
      return null;
    }

    try {
      return constructor.newInstance(parameterValues);
    }
    catch (ReflectiveOperationException | IllegalArgumentException e) {
      log.error("Exception while trying to instantiate a new instance with constructor '{}' and parameters {}.", constructor, parameterValues, e);
      return null;
    }
  }
}
