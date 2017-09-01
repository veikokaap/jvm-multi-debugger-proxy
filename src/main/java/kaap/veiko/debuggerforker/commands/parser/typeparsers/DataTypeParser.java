package kaap.veiko.debuggerforker.commands.parser.typeparsers;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaap.veiko.debuggerforker.VMInformation;
import kaap.veiko.debuggerforker.commands.sets.virtualmachine.IDSizesReplyCommand;
import kaap.veiko.debuggerforker.commands.types.DataType;

public class DataTypeParser<T> implements TypeParser<DataType> {
  
  private final Logger log = LoggerFactory.getLogger(DataTypeParser.class);
  
  private final VMInformation vmInformation;

  public DataTypeParser(VMInformation vmInformation) {
    this.vmInformation = vmInformation;
  }

  @Override
  public DataType parse(ByteBuffer byteBuffer, Class<?> type) throws ReflectiveOperationException {
    if (!DataType.class.isAssignableFrom(type)) {
      log.error("Type '{}' is not a subtype of '{}'", type.getName(), DataType.class.getName());
    }
    
    return (DataType) type.getConstructor(ByteBuffer.class, IDSizesReplyCommand.class)
        .newInstance(byteBuffer, vmInformation.getIdSizes());
  }
}
