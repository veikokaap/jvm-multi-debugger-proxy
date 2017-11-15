package kaap.veiko.debuggerforker.types;

import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DataTypeBase implements DataType {

  private final static Logger log = LoggerFactory.getLogger(DataTypeBase.class);
  private final long value;
  private final int size;

  DataTypeBase(long value, int size) {
    this.value = value;
    this.size = size;
  }

  DataTypeBase(PacketDataReader reader, Function<IdSizes, Integer> sizeFunction) {
    IdSizes idSizes = reader.getIdSizes();

    if (idSizes != null) {
      size = sizeFunction.apply(idSizes);
    }
    else {
      size = 8;
      log.warn("Parsing value without knowing its size in bytes. Assuming size is 8 bytes.");
    }

    value = reader.readLongOfSize(size);
  }

  @Override
  public void write(PacketDataWriter writer) {
    writer.writeLongOfSize(value, size);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    DataTypeBase that = (DataTypeBase) o;

    return value == that.value;
  }

  @Override
  public int hashCode() {
    return (int) (value ^ (value >>> 32));
  }

  public long asLong() {
    return value;
  }

  @Override
  public String toString() {
    return this.getClass().getSimpleName() + "{" +
        "value=" + value +
        '}';
  }
}
