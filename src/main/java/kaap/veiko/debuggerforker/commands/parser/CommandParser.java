package kaap.veiko.debuggerforker.commands.parser;

import kaap.veiko.debuggerforker.commands.Command;
import kaap.veiko.debuggerforker.packet.Packet;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.nio.ByteBuffer;
import java.util.Set;
import java.util.stream.Collectors;

public class CommandParser {
    public Command parse(Packet packet) {
        try {
            Class<?> commandClass = findCommandClass(packet.getCommandSet(), packet.getCommand(), packet.isReply());
            Constructor<?> constructor = commandClass.getConstructors()[0];

            Object[] parameterValues = getConstructorParameterValues(
                    packet.getDataBytes(),
                    constructor.getParameters()
            );

            return (Command) constructor.newInstance(parameterValues);
        } catch (NoCommandException e) {
            System.out.println("Command POJO not yet defined for " +
                    "commandSet " + packet.getCommandSet() +
                    " and command " + packet.getCommand() +
                    " and reply " + packet.isReply()
            );
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Object[] getConstructorParameterValues(byte[] dataBytes, Parameter[] parameters) {
        Object[] parameterValues = new Object[parameters.length];

        ByteBuffer buffer = ByteBuffer.wrap(dataBytes);
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];

            if (parameter.getType().equals(byte.class)) {
                parameterValues[i] = buffer.get();
            } else if (parameter.getType().equals(short.class)) {
                parameterValues[i] = buffer.getShort();
            } else if (parameter.getType().equals(int.class)) {
                parameterValues[i] = buffer.getInt();
            } else if (parameter.getType().equals(long.class)) {
                parameterValues[i] = buffer.getLong();
            }
        }
        return parameterValues;
    }

    private Class<?> findCommandClass(short commandSet, short command, boolean isReplyPacket) throws AmbiguousCommandException, NoCommandException {
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

    private class AmbiguousCommandException extends Exception{
        private AmbiguousCommandException(String message) {
            super(message);
        }
    }

    private class NoCommandException extends Exception{
        private NoCommandException(String message) {
            super(message);
        }
    }
}
