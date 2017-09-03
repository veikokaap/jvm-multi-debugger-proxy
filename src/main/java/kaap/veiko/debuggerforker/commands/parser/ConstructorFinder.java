package kaap.veiko.debuggerforker.commands.parser;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaap.veiko.debuggerforker.commands.parser.annotations.JDWPCommandConstructor;

public class ConstructorFinder {

  private final Logger log = LoggerFactory.getLogger(ConstructorFinder.class);

  public Constructor<?> find(Class<?> commandClass) {
    Set<Constructor<?>> matchingConstructors = Arrays.stream(commandClass.getConstructors())
        .filter(it -> it.isAnnotationPresent(JDWPCommandConstructor.class))
        .collect(Collectors.toSet());

    if (matchingConstructors.isEmpty()) {
      log.warn("No constructor with annotation {} found in class '{}'",
          JDWPCommandConstructor.class.getSimpleName(), commandClass.getName());
      return null;
    }
    else if (matchingConstructors.size() > 1) {
      log.warn("More than one constructor with annotation {} found in class '{}'",
          JDWPCommandConstructor.class.getSimpleName(), commandClass.getName());
    }

    Constructor<?> matchingConstructor = matchingConstructors.iterator().next();
    log.debug("For class '{}', found constructor '{}'", commandClass.getName(), matchingConstructor);

    return matchingConstructor;
  }
}
