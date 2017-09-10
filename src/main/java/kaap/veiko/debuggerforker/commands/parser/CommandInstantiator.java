package kaap.veiko.debuggerforker.commands.parser;

import java.lang.reflect.Constructor;
import java.nio.ByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandInstantiator {

  private final static Logger log = LoggerFactory.getLogger(CommandInstantiator.class);

  private final ParameterParser parameterParser;

  public CommandInstantiator(ParameterParser parameterParser) {
    this.parameterParser = parameterParser;
  }

  public <T> T newInstanceFromBytes(Constructor<T> constructor, byte[] bytes) {
    return newInstanceFromByteBuffer(constructor, ByteBuffer.wrap(bytes));
  }

  public <T> T newInstanceFromByteBuffer(Constructor<T> constructor, ByteBuffer dataBuffer) {
    Object[] parameterValues;
    try {
      parameterValues = parameterParser.parseMultipleValuesFromBuffer(
          dataBuffer,
          constructor.getParameters()
      );
    }
    catch (ReflectiveOperationException e) {
      log.error("Exception while trying to parse values for a Command constructor '{}'.", constructor, e);
      return null;
    }

    try {
      return constructor.newInstance(parameterValues);
    }
    catch (ReflectiveOperationException | IllegalArgumentException e) {
      log.error("Exception while trying to instantiate a new instance with constructor '{}' and parameters {}.", constructor, parameterValues, e);
      return null;
    }
  }
}
