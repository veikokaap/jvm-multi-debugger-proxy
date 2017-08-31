package kaap.veiko.debuggerforker.commands.parser;

import kaap.veiko.debuggerforker.commands.parser.annotations.JDWPCommand;
import org.reflections.Reflections;

import java.util.Set;
import java.util.stream.Collectors;

public class CommandUtil {
    public static Class<?> findCommandClass(short commandSet, short command, boolean isReplyPacket) throws AmbiguousCommandException, NoCommandException {
        Set<Class<?>> typesAnnotatedWith = new Reflections("kaap.veiko.debuggerforker.commands").getTypesAnnotatedWith(JDWPCommand.class);

        Set<Class<?>> annotatedClasses = typesAnnotatedWith.stream()
                .filter(clazz -> {
                    JDWPCommand annotation = clazz.getAnnotation(JDWPCommand.class);
                    return annotation.command() == command
                            && annotation.commandSet() == commandSet
                            && annotation.commandType().check(isReplyPacket);
                })
                .collect(Collectors.toSet());

        if (annotatedClasses.size() > 1) {
            throw new AmbiguousCommandException(
                    "Ambiguous command for commandSet " + commandSet +
                            " and command " + command +
                            " and commandType " + isReplyPacket +
                            ". Classes that match: " + annotatedClasses
            );
        } else if (annotatedClasses.isEmpty()) {
            throw new NoCommandException("No command for commandSet " + commandSet +
                    " and command " + command +
                    " and commandType " + isReplyPacket
            );
        }

        return annotatedClasses.iterator().next();
    }

    public static class AmbiguousCommandException extends Exception {
        private AmbiguousCommandException(String message) {
            super(message);
        }
    }

    public static class NoCommandException extends Exception {
        private NoCommandException(String message) {
            super(message);
        }
    }
}
