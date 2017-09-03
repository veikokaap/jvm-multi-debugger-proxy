package kaap.veiko.debuggerforker.commands.types;

import java.nio.ByteBuffer;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaap.veiko.debuggerforker.commands.sets.virtualmachine.IDSizesReplyCommand;
import kaap.veiko.debuggerforker.utils.ByteBufferUtil;

public abstract class DataTypeBase implements DataType {

  private final static Logger log = LoggerFactory.getLogger(DataTypeBase.class);
  private final long value;

  public DataTypeBase(ByteBuffer buffer, IDSizesReplyCommand idSizes, Function<IDSizesReplyCommand, Integer> sizeFunction) {
    if (idSizes != null) {
      int size = sizeFunction.apply(idSizes);
      value = ByteBufferUtil.getLong(buffer, size);
    }
    else {
      log.warn("Parsing value without knowing its size in bytes. Assuming size is 8 bytes.");
      value = buffer.getLong();
    }
  }

  @Override
  public long asLong() {
    return value;
  }

  @Override
  public String toString() {
    return "DataTypeBase{" +
        "value=" + value +
        '}';
  }
}
