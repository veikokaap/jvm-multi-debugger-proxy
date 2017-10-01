package kaap.veiko.debuggerforker.commands.parser.typeparsers;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaap.veiko.debuggerforker.commands.parser.CommandInstantiator;
import kaap.veiko.debuggerforker.commands.parser.ConstructorFinder;
import kaap.veiko.debuggerforker.commands.parser.ParameterParser;
import kaap.veiko.debuggerforker.commands.parser.annotations.JDWPAbstractCommandContent;
import kaap.veiko.debuggerforker.commands.parser.annotations.JDWPArray;
import kaap.veiko.debuggerforker.commands.parser.annotations.JDWPSubCommand;

public class ArrayParser implements TypeParser<Object[]> {

  private final static Logger log = LoggerFactory.getLogger(ArrayParser.class);

  private final ConstructorFinder constructorFinder = new ConstructorFinder();
  private final ParameterParser parameterParser;
  private final CommandInstantiator commandInstantiator;

  public ArrayParser(ParameterParser parameterParser) {
    this.parameterParser = parameterParser;
    this.commandInstantiator = new CommandInstantiator(parameterParser);
  }

  @Override
  public boolean hasCorrectType(Class<?> type) {
    return type != null && type.isArray();
  }

  @Override
  public Object[] parse(ByteBuffer byteBuffer, Parameter parameter) throws ReflectiveOperationException {
    if (!parameter.isAnnotationPresent(JDWPArray.class)) {
      log.error("Array type parameter '{}' doesn't have annotation '{}'", parameter, JDWPArray.class.getSimpleName());
      return null;
    }

    Class<? extends Number> counterType = parameter.getAnnotation(JDWPArray.class).counterType();
    int count = (int) parameterParser.getValueFromBuffer(byteBuffer, counterType);

    return parseArray(byteBuffer, parameter.getType(), count);
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
        repetitiveDataArray[l] = commandInstantiator.newInstanceFromByteBuffer(constructor, buffer);
      }
      else {
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
      Object parameterValue = parameterParser.getValueFromBuffer(buffer, identifierClass);
      if (parameterValue != null && parameterValue instanceof Number) {
        identifier = ((Number) parameterValue).longValue();
      }
    }

    long finalIdentifier = identifier;
    Optional<Class<?>> any = subTypesOfRepetitiveData.stream()
        .filter(clazz -> clazz.getAnnotation(JDWPSubCommand.class).eventKind().getId() == finalIdentifier)
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
