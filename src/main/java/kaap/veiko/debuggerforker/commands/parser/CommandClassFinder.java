package kaap.veiko.debuggerforker.commands.parser;

import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaap.veiko.debuggerforker.commands.Command;
import kaap.veiko.debuggerforker.commands.parser.annotations.JDWPCommand;
import kaap.veiko.debuggerforker.commands.sets.CommandIdentifier;
import kaap.veiko.debuggerforker.packet.Packet;

public class CommandClassFinder {

  private final Logger log = LoggerFactory.getLogger(CommandClassFinder.class);
  private Reflections reflections = new Reflections("kaap.veiko.debuggerforker.commands");


  public Class<? extends Command> find(Packet packet) {
    CommandType commandType = CommandType.COMMAND;
    if (packet.isReply()) {
      commandType = CommandType.REPLY;
    }

    return find(packet.getCommandSet(), packet.getCommand(), commandType);
  }

  private Class<? extends Command> find(short commandSet, short command, CommandType commandType) {
    Set<Class<? extends Command>> matchingCommandClasses = reflections
        .getSubTypesOf(Command.class).stream()
        .filter(clazz -> clazz.isAnnotationPresent(JDWPCommand.class))
        .filter(clazz -> {
          JDWPCommand annotation = clazz.getAnnotation(JDWPCommand.class);
          CommandIdentifier identifier = annotation.value();

          return identifier.getCommandSetId() == commandSet
              && identifier.getCommandId() == command
              && identifier.getType() == commandType;
        })
        .collect(Collectors.toSet());

    if (matchingCommandClasses.isEmpty()) {
      log.warn("No command class found for commandSet '{}', command '{}' and type '{}'",
          commandSet, command, commandType);
      return null;
    }
    else if (matchingCommandClasses.size() > 1) {
      log.warn("More than one command class found for commandSet '{}', command '{}' and type '{}'. Found classes: {}",
          commandSet, command, commandType, matchingCommandClasses);
      return null;
    }

    Class<? extends Command> matchingClass = matchingCommandClasses.iterator().next();
    log.debug("For commandSet '{}', command '{}' and type '{}', found the class {}",
        commandSet, command, commandType, matchingClass.getName());

    return matchingClass;
  }
}
