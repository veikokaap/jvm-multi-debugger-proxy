package kaap.veiko.debuggerforker.commands.parser;

import kaap.veiko.debuggerforker.VMInformation;
import kaap.veiko.debuggerforker.commands.Command;
import kaap.veiko.debuggerforker.commands.parser.annotations.*;
import kaap.veiko.debuggerforker.commands.parser.typeparsers.BooleanParser;
import kaap.veiko.debuggerforker.commands.parser.typeparsers.ByteBarser;
import kaap.veiko.debuggerforker.commands.parser.typeparsers.DataTypeParser;
import kaap.veiko.debuggerforker.commands.parser.typeparsers.IntParser;
import kaap.veiko.debuggerforker.commands.parser.typeparsers.LongParser;
import kaap.veiko.debuggerforker.commands.parser.typeparsers.ShortParser;
import kaap.veiko.debuggerforker.commands.parser.typeparsers.StringParser;
import kaap.veiko.debuggerforker.commands.parser.typeparsers.TypeParser;
import kaap.veiko.debuggerforker.commands.types.DataType;
import kaap.veiko.debuggerforker.packet.Packet;

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

public class CommandParser {

    private final static Logger log = LoggerFactory.getLogger(CommandParser.class);

    private final ConcurrentMap<Class<?>, TypeParser> parserHashMap = new ConcurrentHashMap<>();
    private final ConstructorFinder constructorFinder = new ConstructorFinder();
    private final CommandClassFinder commandClassFinder = new CommandClassFinder();
    private final VMInformation vmInformation;

    public CommandParser(VMInformation vmInformation) {
        this.vmInformation = vmInformation;
        this.parserHashMap.put(byte.class, new ByteBarser());
        this.parserHashMap.put(boolean.class, new BooleanParser());
        this.parserHashMap.put(short.class, new ShortParser());
        this.parserHashMap.put(int.class, new IntParser());
        this.parserHashMap.put(long.class, new LongParser());
        this.parserHashMap.put(String.class, new StringParser());
        this.parserHashMap.put(DataType.class, new DataTypeParser(vmInformation));
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
        } else if (!(command instanceof Command)) {
            log.error("'{}' isn't an instance of '{}'", command, Command.class.getName());
            return null;
        }

        return (Command) command;
    }

    private Object newInstanceFromByteBuffer(Constructor<?> constructor, ByteBuffer dataBuffer) {
        Object[] parameterValues;
        try {
            parameterValues = getConstructorParameterValues(
                    dataBuffer,
                    constructor.getParameters()
            );
        } catch (ReflectiveOperationException e) {
            log.error("Exception while trying to newInstanceFromByteBuffer values for a Command constructor '{}'.", constructor, e);
            return null;
        }

        try {
            return constructor.newInstance(parameterValues);
        } catch (ReflectiveOperationException | IllegalArgumentException e) {
            log.error("Exception while trying to instantiate a new instance with constructor '{}' and parameters {}.", constructor, parameterValues, e);
            return null;
        }
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
                Constructor<?> constructor = constructorFinder.find(clazz);
                repetitiveDataArray[l] = newInstanceFromByteBuffer(constructor, buffer);
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
}
