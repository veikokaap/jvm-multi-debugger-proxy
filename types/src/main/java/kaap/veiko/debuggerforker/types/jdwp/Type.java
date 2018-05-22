package kaap.veiko.debuggerforker.types.jdwp;

import java.util.Arrays;
import java.util.function.BiConsumer;
import org.checkerframework.checker.nullness.qual.Nullable;

import kaap.veiko.debuggerforker.types.DataReadException;
import kaap.veiko.debuggerforker.types.DataReader;
import kaap.veiko.debuggerforker.types.DataWriter;

public enum Type {
  ARRAY(91, ObjectId::read, DataWriter::writeType),
  BYTE(66, DataReader::readByte, DataWriter::writeByte),
  CHAR(67, DataReader::readShort, DataWriter::writeShort),
  OBJECT(76, ObjectId::read, DataWriter::writeType),
  FLOAT(70, DataReader::readFloat, DataWriter::writeFloat),
  DOUBLE(68, DataReader::readDouble, DataWriter::writeDouble),
  INT(73, DataReader::readInt, DataWriter::writeInt),
  LONG(74, DataReader::readLong, DataWriter::writeLong),
  SHORT(83, DataReader::readShort, DataWriter::writeShort),
  VOID(86, reader -> null, (writer, o) -> {
  }),
  BOOLEAN(90, DataReader::readBoolean, DataWriter::writeBoolean),
  STRING(115, ObjectId::read, DataWriter::writeType),
  THREAD(116, ObjectId::read, DataWriter::writeType),
  THREAD_GROUP(103, ObjectId::read, DataWriter::writeType),
  CLASS_LOADER(108, ObjectId::read, DataWriter::writeType),
  CLASS_OBJECT(99, ObjectId::read, DataWriter::writeType);

  private final byte id;
  private final DataTypeReadFunction<@Nullable Object> readFunction;
  private final BiConsumer<DataWriter, @Nullable Object> writeFunction;

  <T> Type(int value, DataTypeReadFunction<@Nullable T> readFunction, BiConsumer<DataWriter, T> writeConsumer) {
    this.id = (byte) value;
    this.readFunction = (DataTypeReadFunction<@Nullable Object>) readFunction;
    this.writeFunction = (BiConsumer<DataWriter, @Nullable Object>) writeConsumer;
  }

  public static Type findByValue(byte value) throws DataReadException {
    return Arrays.stream(Type.values())
        .filter(type -> type.id == value)
        .findFirst()
        .orElseThrow(() -> new DataReadException("Failed to find EventKind for value '" + value + "'."));
  }

  public @Nullable Object read(DataReader reader) throws DataReadException {
    return readFunction.apply(reader);
  }

  public void write(DataWriter writer, @Nullable Object value) {
    writeFunction.accept(writer, value);
  }

  public byte getId() {
    return id;
  }

  @FunctionalInterface
  private interface DataTypeReadFunction<T> {
    T apply(DataReader reader) throws DataReadException;
  }
}
