package kaap.veiko.debuggerforker.commands.parser;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaap.veiko.debuggerforker.commands.parser.annotations.JdwpCommandConstructor;

public class ConstructorFinder {

  private final Logger log = LoggerFactory.getLogger(ConstructorFinder.class);

  public <T> Constructor<T> find(Class<T> commandClass) {
    Set<Constructor<T>> matchingConstructors = Arrays.stream(commandClass.getConstructors())
        .map(constructors -> (Constructor<T>) constructors)
        .filter(it -> it.isAnnotationPresent(JdwpCommandConstructor.class))
        .collect(Collectors.toSet());

    if (matchingConstructors.isEmpty()) {
      log.warn("No constructor with annotation {} found in class '{}'",
          JdwpCommandConstructor.class.getSimpleName(), commandClass.getName());
      return null;
    }
    else if (matchingConstructors.size() > 1) {
      log.warn("More than one constructor with annotation {} found in class '{}'",
          JdwpCommandConstructor.class.getSimpleName(), commandClass.getName());
    }

    Constructor<T> matchingConstructor = matchingConstructors.iterator().next();
    log.debug("For class '{}', found constructor '{}'", commandClass.getName(), matchingConstructor);

    return matchingConstructor;
  }
}
