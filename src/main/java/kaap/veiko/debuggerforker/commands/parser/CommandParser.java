package kaap.veiko.debuggerforker.commands.parser;

import kaap.veiko.debuggerforker.DebuggerForker;
import kaap.veiko.debuggerforker.commands.Command;
import kaap.veiko.debuggerforker.commands.parser.annotations.ArrayCounter;
import kaap.veiko.debuggerforker.commands.parser.annotations.JDWPAbstractCommandContent;
import kaap.veiko.debuggerforker.commands.parser.annotations.JDWPCommandConstructor;
import kaap.veiko.debuggerforker.commands.parser.annotations.JDWPSubCommand;
import kaap.veiko.debuggerforker.commands.sets.virtualmachine.IDSizesReplyCommand;
import kaap.veiko.debuggerforker.commands.types.DataType;
import kaap.veiko.debuggerforker.packet.Packet;
import kaap.veiko.debuggerforker.utils.ByteBufferUtil;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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

    public Command parse(Packet packet) {
        try {
            Class<?> commandClass = CommandUtil.findCommandClass(packet.getCommandSet(), packet.getCommand(), packet.isReply());
            Constructor<?> constructor = findConstructor(commandClass);

            ByteBuffer dataBuffer = ByteBuffer.wrap(packet.getDataBytes());
            Object[] parameterValues = getConstructorParameterValues(
                    dataBuffer,
                    constructor.getParameters()
            );

            return (Command) constructor.newInstance(parameterValues);
        } catch (Exception e) {
            log.error("Exception while parsing the command", e);
            return null;
        }
    }

    private Constructor<?> findConstructor(Class<?> commandClass) throws ReflectiveOperationException {
        Optional<Constructor<?>> constructorOptional = Arrays.stream(commandClass.getConstructors())
                .filter(it -> it.isAnnotationPresent(JDWPCommandConstructor.class))
                .findFirst();

        if (!constructorOptional.isPresent()) {
            throw new ReflectiveOperationException("No constructor found for '" + commandClass.getName() + "'");
        }

        return constructorOptional.get();
    }

    private Object[] getConstructorParameterValues(ByteBuffer dataBuffer, Parameter[] parameters) throws ReflectiveOperationException, IOException {
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

    private Object getParameterValue(ByteBuffer dataBuffer, Class<?> parameterType, Optional<Integer> arrayCounterValue) throws ReflectiveOperationException, IOException {
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

    private Object[] parseArray(ByteBuffer buffer, Class<?> parameterType, int count) throws ReflectiveOperationException, IOException {
        Class<?> componentType = parameterType.getComponentType();
        Class<?> identifierClass = findIdentifierClass(componentType);
        Object[] repetitiveDataArray = (Object[]) Array.newInstance(componentType, count);

        for (int l = 0; l < count; l++) {
            Optional<Class<?>> subClass = findCorrectSubClass(buffer, componentType, identifierClass);
            if (subClass.isPresent()) {
                Class<?> clazz = subClass.get();
                Constructor<?> constructor = findConstructor(clazz);
                repetitiveDataArray[l] = constructor.newInstance(getConstructorParameterValues(buffer, constructor.getParameters()));
            } else {
                repetitiveDataArray[l] = null;
            }
        }
        return repetitiveDataArray;
    }

    private Class<?> findIdentifierClass(Class<?> componentType) {
        return componentType.getAnnotation(JDWPAbstractCommandContent.class).identifierClass();
    }

    private Optional<Class<?>> findCorrectSubClass(ByteBuffer buffer, Class<?> componentType, Class<?> identifierClass) throws ReflectiveOperationException, IOException {
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
        Object parse(ByteBuffer byteBuffer, Class<?> type) throws IOException, ReflectiveOperationException;
    }
}
