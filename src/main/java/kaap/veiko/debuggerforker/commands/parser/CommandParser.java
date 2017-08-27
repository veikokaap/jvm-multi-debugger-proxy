package kaap.veiko.debuggerforker.commands.parser;

import kaap.veiko.debuggerforker.DebuggerForker;
import kaap.veiko.debuggerforker.commands.Command;
import kaap.veiko.debuggerforker.commands.sets.virtualmachine.IDSizesReplyCommand;
import kaap.veiko.debuggerforker.commands.types.DataType;
import kaap.veiko.debuggerforker.packet.Packet;
import org.reflections.Reflections;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.nio.ByteBuffer;
import java.util.Set;
import java.util.stream.Collectors;

public class CommandParser {

    private final DebuggerForker forker;

    public CommandParser(DebuggerForker forker) {
        this.forker = forker;
    }

    public Command parse(Packet packet) {
        try {
            Class<?> commandClass = findCommandClass(packet.getCommandSet(), packet.getCommand(), packet.isReply());
            Constructor<?> constructor = commandClass.getConstructors()[0];

            ByteBuffer dataBuffer = ByteBuffer.wrap(packet.getDataBytes());
            Object[] parameterValues = getConstructorParameterValues(
                    dataBuffer,
                    constructor.getParameters()
            );

            return (Command) constructor.newInstance(parameterValues);
        } catch (NoCommandException e) {
//            System.out.println("Command POJO not yet defined for " +
//                    "commandSet " + packet.getCommandSet() +
//                    " and command " + packet.getCommand() +
//                    " and reply " + packet.isReply()
//            );
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Object[] getConstructorParameterValues(ByteBuffer dataBuffer, Parameter[] parameters) throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        Object[] parameterValues = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];

            if (parameter.getType().equals(byte.class)) {
                parameterValues[i] = dataBuffer.get();
            } else if (parameter.getType().equals(short.class)) {
                parameterValues[i] = dataBuffer.getShort();
            } else if (parameter.getType().equals(int.class)) {
                parameterValues[i] = dataBuffer.getInt();
            } else if (parameter.getType().equals(long.class)) {
                parameterValues[i] = dataBuffer.getLong();
            } else if (parameter.getType().isArray()) {
                int count = ((Number) parameterValues[i - 1]).intValue();
                parameterValues[i] = getArray(dataBuffer, parameter, count);
            } else if (DataType.class.isAssignableFrom(parameter.getType())) {
                parameterValues[i] = getDataType(dataBuffer, parameter);
            }
        }

        return parameterValues;
    }

    private Object getDataType(ByteBuffer dataBuffer, Parameter parameter) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return parameter.getType()
                .getConstructor(ByteBuffer.class, IDSizesReplyCommand.class)
                .newInstance(dataBuffer, forker.getIdSizes());
    }


    private Object[] getArray(ByteBuffer buffer, Parameter parameter, int count) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Class<?> componentType = parameter.getType().getComponentType();
        Set<Class<?>> subTypesOfRepetitiveData = (Set<Class<?>>) new Reflections("kaap.veiko.debuggerforker.commands").getSubTypesOf(componentType);

        Class<?> identifierClass = componentType.getAnnotation(JDWPAbstractCommandContent.class).identifierClass();
        Object[] repetitiveDataArray = (Object[]) Array.newInstance(componentType, count);

        for (int l = 0; l < count; l++) {
            long identifier;

            if (identifierClass.equals(byte.class)) {
                identifier = buffer.get();
            } else if (identifierClass.equals(short.class)) {
                identifier = buffer.getShort();
            } else if (identifierClass.equals(int.class)) {
                identifier = buffer.getInt();
            } else if (identifierClass.equals(long.class)) {
                identifier = buffer.getLong();
            } else {
                identifier = -1;
            }

            Class<?> found = subTypesOfRepetitiveData.stream()
                    .filter(clazz -> clazz.getAnnotation(JDWPCommandContent.class).id() == identifier)
                    .findFirst().get();

            repetitiveDataArray[l] = found.getConstructors()[0].newInstance(getConstructorParameterValues(buffer, found.getConstructors()[0].getParameters()));
        }
        return repetitiveDataArray;
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

    private class AmbiguousCommandException extends Exception {
        private AmbiguousCommandException(String message) {
            super(message);
        }
    }

    private class NoCommandException extends Exception {
        private NoCommandException(String message) {
            super(message);
        }
    }
}
