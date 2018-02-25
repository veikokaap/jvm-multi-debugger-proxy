package kaap.veiko.debuggerforker.types.jdwp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaap.veiko.debuggerforker.types.DataReader;
import kaap.veiko.debuggerforker.types.DataType;
import kaap.veiko.debuggerforker.types.DataWriter;

abstract class DataTypeBase implements DataType {

  private final static Logger log = LoggerFactory.getLogger(DataTypeBase.class);
  private final long value;
  private final IdSizes.SizeType sizeType;

  DataTypeBase(DataReader reader, IdSizes.SizeType sizeType) {
    this.sizeType = sizeType;
    this.value = reader.readLongOfSize(sizeType);
  }

  DataTypeBase(long value, IdSizes.SizeType sizeType) {
    this.value = value;
    this.sizeType = sizeType;
  }

  @Override
  public void write(DataWriter writer) {
    writer.writeLongOfSize(value, sizeType);
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
