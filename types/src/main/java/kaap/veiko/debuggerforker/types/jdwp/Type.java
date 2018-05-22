package kaap.veiko.debuggerforker.types.jdwp;

import java.util.Arrays;
import java.util.function.BiConsumer;
import org.checkerframework.checker.nullness.qual.Nullable;

import kaap.veiko.debuggerforker.types.DataReadException;
import kaap.veiko.debuggerforker.types.DataReader;
import kaap.veiko.debuggerforker.types.DataType;
import kaap.veiko.debuggerforker.types.DataWriter;

public final class Type<T> {

  /* Primitive types */
  public static final Type<@Nullable Void> VOID = voidType((byte) 86);
  public static final Type<Byte> BYTE = primitiveType((byte) 66, DataReader::readByte, DataWriter::writeByte);
  public static final Type<Short> SHORT = primitiveType((byte) 83, DataReader::readShort, DataWriter::writeShort);
  public static final Type<Character> CHAR = primitiveType((byte) 67, DataReader::readChar, DataWriter::writeChar);
  public static final Type<Integer> INT = primitiveType((byte) 73, DataReader::readInt, DataWriter::writeInt);
  public static final Type<Long> LONG = primitiveType((byte) 74, DataReader::readLong, DataWriter::writeLong);
  public static final Type<Float> FLOAT = primitiveType((byte) 70, DataReader::readFloat, DataWriter::writeFloat);
  public static final Type<Double> DOUBLE = primitiveType((byte) 68, DataReader::readDouble, DataWriter::writeDouble);
  public static final Type<Boolean> BOOLEAN = primitiveType((byte) 90, DataReader::readBoolean, DataWriter::writeBoolean);

  /* Object types */
  public static final Type<ObjectId> OBJECT = objectType((byte) 76, ObjectId::read, DataWriter::writeType);
  public static final Type<ArrayId> ARRAY = objectType((byte) 91, ArrayId::read, DataWriter::writeType);
  public static final Type<StringId> STRING = objectType((byte) 115, StringId::read, DataWriter::writeType);
  public static final Type<ThreadId> THREAD = objectType((byte) 116, ThreadId::read, DataWriter::writeType);
  public static final Type<ThreadGroupId> THREAD_GROUP = objectType((byte) 103, ThreadGroupId::read, DataWriter::writeType);
  public static final Type<ClassLoaderId> CLASS_LOADER = objectType((byte) 108, ClassLoaderId::read, DataWriter::writeType);
  public static final Type<ClassObjectId> CLASS_OBJECT = objectType((byte) 99, ClassObjectId::read, DataWriter::writeType);

  private static final Type[] allTypes = new Type[]{
      BYTE, CHAR, DOUBLE, FLOAT, INT, LONG, SHORT, VOID, BOOLEAN, OBJECT, ARRAY, STRING, THREAD, THREAD_GROUP, CLASS_LOADER, CLASS_OBJECT
  };

  public static Type findById(byte id) throws DataReadException {
    return Arrays.stream(allTypes)
        .filter(type -> type.getId() == id)
        .findFirst()
        .orElseThrow(() -> new DataReadException("Failed to find EventKind for id '" + id + "'."));
  }

  private final byte id;
  private final boolean primitive;
  private final DataTypeReadFunction<T> readFunction;
  private final BiConsumer<DataWriter, T> writeFunction;

  private Type(byte id, boolean primitive, DataTypeReadFunction<T> readFunction, BiConsumer<DataWriter, T> writeFunction) {
    this.id = id;
    this.primitive = primitive;
    this.readFunction = readFunction;
    this.writeFunction = writeFunction;
  }

  private static <T extends DataType> Type<T> objectType(byte id, DataTypeReadFunction<T> readFunction, BiConsumer<DataWriter, T> writeFunction) {
    return new Type<T>(id, false, readFunction, writeFunction);
  }

  private static <T> Type<T> primitiveType(byte id, DataTypeReadFunction<T> readFunction, BiConsumer<DataWriter, T> writeFunction) {
    return new Type<T>(id, true, readFunction, writeFunction);
  }

  private static <@Nullable T> Type<T> voidType(byte id) {
    return new Type<T>(id, true,
        reader -> null,
        (dataWriter, value) -> {
        }
    );
  }

  public byte getId() {
    return id;
  }

  public boolean isPrimitive() {
    return primitive;
  }

  public @Nullable T read(DataReader reader) throws DataReadException {
    return readFunction.apply(reader);
  }

  public void write(DataWriter writer, T value) {
    writeFunction.accept(writer, value);
  }

  @FunctionalInterface
  private interface DataTypeReadFunction<T> {
    T apply(DataReader reader) throws DataReadException;
  }
}
