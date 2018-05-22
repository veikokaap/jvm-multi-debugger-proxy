package kaap.veiko.debuggerforker.types.jdwp;

import org.checkerframework.checker.nullness.qual.Nullable;

import kaap.veiko.debuggerforker.types.DataReadException;
import kaap.veiko.debuggerforker.types.DataReader;
import kaap.veiko.debuggerforker.types.DataWriter;

public class UntaggedValue<T> extends Value<T> {
  private UntaggedValue(DataReader reader, byte typeTag) throws DataReadException {
    super(reader, typeTag);
  }

  private UntaggedValue(DataReader reader, Type<T> type) throws DataReadException {
    super(reader, type);
  }

  public UntaggedValue(Type<T> type, T value) {
    super(type, value);
  }

  public static UntaggedValue read(DataReader reader, byte typeTag) throws DataReadException {
    return new UntaggedValue(reader, typeTag);
  }

  public static <T> UntaggedValue<T> read(DataReader reader, Type<T> type) throws DataReadException {
    return new UntaggedValue<>(reader, type);
  }

  @Override
  public void write(DataWriter writer) {
    @Nullable T value = getValue();
    if (value != null) {
      getType().write(writer, value);
    }
    else if (getType() != Type.VOID) {
      throw new NullPointerException("Value contains null for non-void type.");
    }
  }
}
