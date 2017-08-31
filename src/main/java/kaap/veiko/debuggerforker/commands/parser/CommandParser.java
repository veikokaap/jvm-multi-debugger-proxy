package kaap.veiko.debuggerforker.commands.parser;

import kaap.veiko.debuggerforker.DebuggerForker;
import kaap.veiko.debuggerforker.commands.Command;
import kaap.veiko.debuggerforker.commands.parser.annotations.*;
import kaap.veiko.debuggerforker.commands.sets.virtualmachine.IDSizesReplyCommand;
import kaap.veiko.debuggerforker.commands.types.DataType;
import kaap.veiko.debuggerforker.packet.Packet;
import kaap.veiko.debuggerforker.utils.ByteBufferUtil;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class CommandParser {

    private final static Logger log = LoggerFactory.getLogger(CommandParser.class);

    private final DebuggerForker forker;
    private final ConcurrentMap<Class<?>, TypeParser> parserHashMap = new ConcurrentHashMap<>();

    public CommandParser(DebuggerForker forker) {
        this.forker = forker;
        this.parserHashMap.put(byte.class, (byteBuffer, type) -> byteBuffer.get());
        this.parserHashMap.put(boolean.class, (byteBuffer, type) -> byteBuffer.get() != 0);
        this.parserHashMap.put(short.class, (byteBuffer, type) -> byteBuffer.getShort());
        this.parserHashMap.put(int.class, (byteBuffer, type) -> byteBuffer.getInt());
        this.parserHashMap.put(long.class, (byteBuffer, type) -> byteBuffer.getLong());
        this.parserHashMap.put(String.class, ((byteBuffer, type) -> ByteBufferUtil.getString(byteBuffer)));

        this.parserHashMap.put(DataType.class, (byteBuffer, type) ->
                type.getConstructor(ByteBuffer.class, IDSizesReplyCommand.class)
                        .newInstance(byteBuffer, this.forker.getIdSizes())
        );
    }

    public Command parseCommand(Packet packet) {
        Class<?> commandClass = findCommandClass(packet.getCommandSet(), packet.getCommand(), packet.isReply());
        if (commandClass == null) {
            return null;
        }

        Constructor<?> constructor = findConstructor(commandClass);
        if (constructor == null) {
            return null;
        }

        ByteBuffer dataBuffer = ByteBuffer.wrap(packet.getDataBytes());
        Object command = createCommand(constructor, dataBuffer);

        if (command == null) {
            log.error("Parsed command is null. Command class '{}'. Constructor '{}'", commandClass, constructor);
            return null;
        } else if (!(command instanceof Command)) {
            log.error("'{}' isn't an instance of '{}'", command, Command.class.getName());
            return null;
        }

        return (Command) command;
    }

    private Object createCommand(Constructor<?> constructor, ByteBuffer dataBuffer) {
        Object[] parameterValues;
        try {
            parameterValues = getConstructorParameterValues(
                    dataBuffer,
                    constructor.getParameters()
            );
        } catch (ReflectiveOperationException e) {
            log.error("Exception while trying to createCommand values for a Command constructor '{}'.", constructor, e);
            return null;
        }

        try {
            return constructor.newInstance(parameterValues);
        } catch (ReflectiveOperationException | IllegalArgumentException e) {
            log.error("Exception while trying to instantiate a new instance with constructor '{}' and parameters {}.", constructor, parameterValues, e);
            return null;
        }
    }

    private Class<?> findCommandClass(short commandSet, short command, boolean isReplyPacket) {
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

    private Constructor<?> findConstructor(Class<?> commandClass) {
        Set<Constructor<?>> matchingConstructors = Arrays.stream(commandClass.getConstructors())
                .filter(it -> it.isAnnotationPresent(JDWPCommandConstructor.class))
                .collect(Collectors.toSet());

        if (matchingConstructors.isEmpty()) {
            log.warn("No constructor with annotation {} found in class '{}'",
                    JDWPCommandConstructor.class.getSimpleName(), commandClass.getName());
            return null;
        } else if (matchingConstructors.size() > 1) {
            log.warn("More than one constructor with annotation {} found in class '{}'",
                    JDWPCommandConstructor.class.getSimpleName(), commandClass.getName());
        }

        Constructor<?> matchingConstructor = matchingConstructors.iterator().next();
        log.debug("For class '{}', found constructor '{}'", commandClass.getName(), matchingConstructor);

        return matchingConstructor;
    }

    private Object[] getConstructorParameterValues(ByteBuffer dataBuffer, Parameter[] parameters) throws ReflectiveOperationException {
        Object[] parameterValues = new Object[parameters.length];
        Optional<Integer> arrayCounterValue = Optional.empty();

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            parameterValues[i] = getParameterValue(dataBuffer, parameter.getType(), arrayCounterValue);

            if (parameter.isAnnotationPresent(ArrayCounter.class)) {
                arrayCounterValue = Optional.of((Integer) parameterValues[i]);
            }
        }

        return parameterValues;
    }

    private Object getParameterValue(ByteBuffer dataBuffer, Class<?> parameterType, Optional<Integer> arrayCounterValue) throws ReflectiveOperationException {
        TypeParser parser = findParser(parameterType);

        if (parser != null) {
            return parser.parse(dataBuffer, parameterType);
        } else if (parameterType.isArray()) {
            if (arrayCounterValue.isPresent()) {
                int count = arrayCounterValue.get();
                return parseArray(dataBuffer, parameterType, count);
            } else {
                throw new ReflectiveOperationException("No array counter present");
            }
        } else {
            return null;
        }
    }

    private TypeParser findParser(Class type) {
        if (parserHashMap.containsKey(type)) {
            return parserHashMap.get(type);
        }

        for (Class<?> aClass : parserHashMap.keySet()) {
            if (aClass.isAssignableFrom(type) || Arrays.asList(type.getInterfaces()).contains(aClass)) {
                return parserHashMap.get(aClass);
            }
        }

        return null;
    }

    private Object[] parseArray(ByteBuffer buffer, Class<?> parameterType, int count) throws ReflectiveOperationException {
        Class<?> componentType = parameterType.getComponentType();
        Class<?> identifierClass = findIdentifierClass(componentType);
        Object[] repetitiveDataArray = (Object[]) Array.newInstance(componentType, count);

        for (int l = 0; l < count; l++) {
            Optional<Class<?>> subClass = findCorrectSubClass(buffer, componentType, identifierClass);
            if (subClass.isPresent()) {
                Class<?> clazz = subClass.get();
                Constructor<?> constructor = findConstructor(clazz);
                repetitiveDataArray[l] = createCommand(constructor, buffer);
            } else {
                repetitiveDataArray[l] = null;
            }
        }
        return repetitiveDataArray;
    }

    private Class<?> findIdentifierClass(Class<?> componentType) {
        return componentType.getAnnotation(JDWPAbstractCommandContent.class).identifierClass();
    }

    private Optional<Class<?>> findCorrectSubClass(ByteBuffer buffer, Class<?> componentType, Class<?> identifierClass) throws ReflectiveOperationException {
        Set<Class<?>> subTypesOfRepetitiveData =
                (Set<Class<?>>) new Reflections("kaap.veiko.debuggerforker.commands").getSubTypesOf(componentType);

        long identifier = -1;

        if (isNumber(identifierClass)) {
            Object parameterValue = getParameterValue(buffer, identifierClass, Optional.empty());
            if (parameterValue != null && parameterValue instanceof Number) {
                identifier = ((Number) parameterValue).longValue();
            }
        }

        long finalIdentifier = identifier;
        Optional<Class<?>> any = subTypesOfRepetitiveData.stream()
                .filter(clazz -> clazz.getAnnotation(JDWPSubCommand.class).id() == finalIdentifier)
                .findFirst();

        return any;
    }

    private boolean isNumber(Class<?> identifierClass) {
        return Number.class.isAssignableFrom(identifierClass) ||
                identifierClass.equals(byte.class) ||
                identifierClass.equals(short.class) ||
                identifierClass.equals(int.class) ||
                identifierClass.equals(long.class);
    }

    @FunctionalInterface
    public interface TypeParser {
        Object parse(ByteBuffer byteBuffer, Class<?> type) throws ReflectiveOperationException;
    }
}
