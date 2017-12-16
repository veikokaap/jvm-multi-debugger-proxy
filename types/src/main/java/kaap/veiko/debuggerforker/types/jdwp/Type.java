package kaap.veiko.debuggerforker.types.jdwp;

import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Function;

import kaap.veiko.debuggerforker.types.DataReader;
import kaap.veiko.debuggerforker.types.DataWriter;

public enum Type {
  ARRAY(91, (reader) -> reader.readLongOfSize(IdSizes.SizeType.OBJECT_ID_SIZE), (writer,o) -> writer.writeLong((Long)o)),
  BYTE(66, DataReader::readByte, (writer, o) -> writer.writeByte((Byte)o)),
  CHAR(67, DataReader::readShort, (writer, o) -> writer.writeShort((Short)o)),
  OBJECT(76, (reader) -> reader.readLongOfSize(IdSizes.SizeType.OBJECT_ID_SIZE), (writer,o) -> writer.writeLong((Long)o)),
  FLOAT(70, DataReader::readInt, (writer, o) -> writer.writeInt((Integer) o)),
  DOUBLE(68, DataReader::readLong, (writer, o) -> writer.writeLong((Long) o)),
  INT(73, DataReader::readInt, (writer, o) -> writer.writeInt((Integer) o)),
  LONG(74, DataReader::readLong, (writer, o) -> writer.writeLong((Long) o)),
  SHORT(83, DataReader::readShort, (writer, o) -> writer.writeShort((Short)o)),
  VOID(86, reader -> null, (writer, o) -> {}),
  BOOLEAN(90, DataReader::readBoolean, (writer, o) -> writer.writeBoolean((Boolean) o)),
  STRING(115, (reader) -> reader.readLongOfSize(IdSizes.SizeType.OBJECT_ID_SIZE), (writer,o) -> writer.writeLong((Long)o)),
  THREAD(116, (reader) -> reader.readLongOfSize(IdSizes.SizeType.OBJECT_ID_SIZE), (writer,o) -> writer.writeLong((Long)o)),
  THREAD_GROUP(103, (reader) -> reader.readLongOfSize(IdSizes.SizeType.OBJECT_ID_SIZE), (writer,o) -> writer.writeLong((Long)o)),
  CLASS_LOADER(108, (reader) -> reader.readLongOfSize(IdSizes.SizeType.OBJECT_ID_SIZE), (writer,o) -> writer.writeLong((Long)o)),
  CLASS_OBJECT(99, (reader) -> reader.readLongOfSize(IdSizes.SizeType.OBJECT_ID_SIZE), (writer,o) -> writer.writeLong((Long)o));

  private final byte id;
  private final Function<DataReader, Object> readFunction;
  private final BiConsumer<DataWriter, Object> writeFunction;

  Type(int value, Function<DataReader, Object> readFunction, BiConsumer<DataWriter, Object> writeConsumer) {
    this.id = (byte) value;
    this.readFunction = readFunction;
    this.writeFunction = writeConsumer;
  }

  public static Type findByValue(byte value) {
    return Arrays.stream(Type.values())
        .filter(type -> type.id == value)
        .findFirst().get();
  }

  public Object read(DataReader reader) {
    return readFunction.apply(reader);
  }

  public void write(DataWriter writer, Object value) {
    writeFunction.accept(writer, value);
  }

  public byte getId() {
    return id;
  }

  private static class NO_WRITE_CONSUMER implements BiConsumer<DataWriter, Object> {
    @Override
    public void accept(DataWriter dataWriter, Object o) {
    }
  }
}
