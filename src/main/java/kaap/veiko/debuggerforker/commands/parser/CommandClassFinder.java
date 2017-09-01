package kaap.veiko.debuggerforker.commands.parser;

import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaap.veiko.debuggerforker.commands.parser.annotations.JDWPCommand;

public class CommandClassFinder {
  
  private final Logger log = LoggerFactory.getLogger(CommandClassFinder.class);
  
  public Class<?> find(short commandSet, short command, boolean isReplyPacket) {
    Set<Class<?>> matchingCommandClasses = new Reflections("kaap.veiko.debuggerforker.commands").getTypesAnnotatedWith(JDWPCommand.class)
        .stream()
        .filter(clazz -> {
          JDWPCommand annotation = clazz.getAnnotation(JDWPCommand.class);
          return annotation.command() == command
              && annotation.commandSet() == commandSet
              && annotation.commandType().check(isReplyPacket);
        })
        .collect(Collectors.toSet());

    if (matchingCommandClasses.isEmpty()) {
      log.warn("No command class found for commandSet '{}', command '{}' and isReplyPacket '{}'",
          commandSet, command, isReplyPacket);
      return null;
    } else if (matchingCommandClasses.size() > 1) {
      log.warn("More than one command class found for commandSet '{}', command '{}' and isReplyPacket '{}'. Found classes: {}",
          commandSet, command, isReplyPacket, matchingCommandClasses);
      return null;
    }

    Class<?> matchingClass = matchingCommandClasses.iterator().next();
    log.debug("For commandSet '{}', command '{}' and isReplyPacket '{}', found the class {}",
        commandSet, command, isReplyPacket, matchingClass.getName());

    return matchingClass;
  }
}
