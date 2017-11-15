package kaap.veiko.debuggerforker.commands.parser;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaap.veiko.debuggerforker.commands.parser.annotations.IdentifierType;
import kaap.veiko.debuggerforker.commands.parser.annotations.JdwpSubType;
import kaap.veiko.debuggerforker.commands.parser.annotations.JdwpArray;
import kaap.veiko.debuggerforker.types.DataType;

public class ArrayParser implements TypeParser<DataType[]> {

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
    return type != null && type.isArray() && DataType.class.isAssignableFrom(type.getComponentType());
  }

  @Override
  public void putToBuffer(ByteBuffer buffer, DataType[] array) {
    JdwpArray arrayAnnotation = array.getClass().getComponentType().getAnnotation(JdwpArray.class);
    Class<? extends Number> counterType = arrayAnnotation.counterType();

    parameterParser.valueOfTypeToByteBuffer(buffer, counterType, array.length);

    for (DataType value : array) {
      value.putToBuffer(buffer);
    }
  }

  @Override
  public DataType[] parse(ByteBuffer byteBuffer, Parameter parameter) throws ReflectiveOperationException {
    if (!parameter.getType().getComponentType().isAnnotationPresent(JdwpArray.class)) {
      log.error("Array type parameter '{}' doesn't have annotation '{}'", parameter, JdwpArray.class.getSimpleName());
      return null;
    }

    Class<? extends Number> counterType = parameter.getType().getComponentType().getAnnotation(JdwpArray.class).counterType();
    int count = (int) parameterParser.getValueFromBuffer(byteBuffer, counterType);

    return parseArray(byteBuffer, parameter.getType(), count);
  }

  private DataType[] parseArray(ByteBuffer buffer, Class<?> parameterType, int count) throws ReflectiveOperationException {
    Class<DataType> componentType = (Class<DataType>) parameterType.getComponentType();
    Class<? extends Annotation> identifierAnnotationClass = findIdentifierAnnotationClass(componentType);
    DataType[] repetitiveDataArray = (DataType[]) Array.newInstance(componentType, count);

    for (int l = 0; l < count; l++) {
      Optional<Class<? extends DataType>> subClass = findCorrectSubClass(buffer, componentType, identifierAnnotationClass);
      if (subClass.isPresent()) {
        Class<? extends DataType> clazz = subClass.get();
        Constructor<? extends DataType> constructor = constructorFinder.find(clazz);
        repetitiveDataArray[l] = commandInstantiator.newInstanceFromByteBuffer(constructor, buffer);
      }
      else {
        repetitiveDataArray[l] = null;
      }
    }
    return repetitiveDataArray;
  }

  private Class<? extends Annotation> findIdentifierAnnotationClass(Class<?> componentType) {
    return componentType.getAnnotation(JdwpSubType.class).identifierAnnotation();
  }

  private Optional<Class<? extends DataType>> findCorrectSubClass(ByteBuffer buffer, Class<DataType> componentType, Class<? extends Annotation> identifierAnnotationClass) throws ReflectiveOperationException {
    Set<Class<? extends DataType>> subTypesOfRepetitiveData = new Reflections("kaap.veiko.debuggerforker").getSubTypesOf(componentType);

    Object identifier = parameterParser.getValueFromBuffer(buffer, identifierAnnotationClass.getAnnotation(IdentifierType.class).value());

    for (Class<? extends DataType> clazz : subTypesOfRepetitiveData) {
      if (annotationValue(clazz, identifierAnnotationClass).equals(identifier)) {
        return Optional.of(clazz);
      }
    }

    return Optional.empty();
  }

  private Object annotationValue(Class<?> clazz, Class<? extends Annotation> identifierAnnotationClass) throws ReflectiveOperationException {
    return identifierAnnotationClass.getMethod("value").invoke(clazz.getAnnotation(identifierAnnotationClass));
  }

}
