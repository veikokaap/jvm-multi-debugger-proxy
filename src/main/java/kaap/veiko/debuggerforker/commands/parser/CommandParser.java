package kaap.veiko.debuggerforker.commands.parser;

import kaap.veiko.debuggerforker.DebuggerForker;
import kaap.veiko.debuggerforker.commands.Command;
import kaap.veiko.debuggerforker.commands.sets.virtualmachine.IDSizesReplyCommand;
import kaap.veiko.debuggerforker.commands.types.DataType;
import kaap.veiko.debuggerforker.packet.Packet;
import kaap.veiko.debuggerforker.utils.ByteBufferUtil;
import org.reflections.Reflections;

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
            Constructor<?> constructor = commandClass.getConstructors()[0];

            ByteBuffer dataBuffer = ByteBuffer.wrap(packet.getDataBytes());
            Object[] parameterValues = getConstructorParameterValues(
                    dataBuffer,
                    constructor.getParameters()
            );

            return (Command) constructor.newInstance(parameterValues);
        } catch (CommandUtil.NoCommandException e) {
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

    private Object[] getConstructorParameterValues(ByteBuffer dataBuffer, Parameter[] parameters) throws ReflectiveOperationException, IOException {
        Object[] parameterValues = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Object previousParameterValue = null;
            if (i > 0) {
                previousParameterValue = parameterValues[i - 1];
            }

            parameterValues[i] = getParameterValue(dataBuffer, parameter.getType(), previousParameterValue);
        }

        return parameterValues;
    }

    private Object getParameterValue(ByteBuffer dataBuffer, Class<?> parameterType, Object previousParameterValue) throws ReflectiveOperationException, IOException {
        TypeParser parser = findParser(parameterType);

        if (parser != null) {
            return parser.parse(dataBuffer, parameterType);
        } else if (parameterType.isArray()) {
            int count = ((Number) previousParameterValue).intValue();
            return parseArray(dataBuffer, parameterType, count);
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
        Set<Class<?>> subTypesOfRepetitiveData =
                (Set<Class<?>>) new Reflections("kaap.veiko.debuggerforker.commands").getSubTypesOf(componentType);

        Class<?> identifierClass = componentType.getAnnotation(JDWPAbstractCommandContent.class).identifierClass();
        Object[] repetitiveDataArray = (Object[]) Array.newInstance(componentType, count);

        for (int l = 0; l < count; l++) {
            long identifier = -1;

            if (isNumber(identifierClass)) {
                Object parameterValue = getParameterValue(buffer, identifierClass, null);
                if (parameterValue != null && parameterValue instanceof Number) {
                    identifier = ((Number) parameterValue).longValue();
                }
            }

            long finalIdentifier = identifier;
            Optional<Class<?>> optional = subTypesOfRepetitiveData.stream()
                    .filter(clazz -> clazz.getAnnotation(JDWPCommandContent.class).id() == finalIdentifier)
                    .findFirst();
            if (optional.isPresent()) {
                Class<?> found = optional.get();
                repetitiveDataArray[l] = found.getConstructors()[0].newInstance(getConstructorParameterValues(buffer, found.getConstructors()[0].getParameters()));
            } else {
                System.out.println("Failed to find JDWPCommandContent for superclass '" + componentType.getSimpleName() + "' with identifier '" + identifier + "'");
                repetitiveDataArray[l] = null;
            }
        }
        return repetitiveDataArray;
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
