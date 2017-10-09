package kaap.veiko.debuggerforker.commands.parser.typeparsers;

import java.lang.reflect.Parameter;
import java.nio.ByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaap.veiko.debuggerforker.VMInformation;
import kaap.veiko.debuggerforker.commands.IdSizes;
import kaap.veiko.debuggerforker.commands.types.DataType;

public class DataTypeParser implements TypeParser<DataType> {

  private final Logger log = LoggerFactory.getLogger(DataTypeParser.class);

  private final VMInformation vmInformation;

  public DataTypeParser(VMInformation vmInformation) {
    this.vmInformation = vmInformation;
  }

  @Override
  public DataType parse(ByteBuffer byteBuffer, Parameter parameter) throws ReflectiveOperationException {
    if (!DataType.class.isAssignableFrom(parameter.getType())) {
      log.error("Type '{}' is not a subtype of '{}'", parameter.getName(), DataType.class.getName());
    }

    return (DataType) parameter.getType().getConstructor(ByteBuffer.class, IdSizes.class)
        .newInstance(byteBuffer, vmInformation.getIdSizes());
  }

  @Override
  public boolean hasCorrectType(Class<?> type) {
    return DataType.class.isAssignableFrom(type);
  }
}
