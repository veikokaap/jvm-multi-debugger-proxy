package kaap.veiko.debuggerforker.types;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import kaap.veiko.debuggerforker.utils.ByteBufferUtil;


public class PacketDataReader {
  private final ByteBuffer byteBuffer;
  private final VMInformation vmInformation;
  private final Map<Class<? extends DataType>, Function<PacketDataReader, ? extends DataType>> dataTypeParserMap;

  public PacketDataReader(ByteBuffer packetByteBuffer, VMInformation vmInformation) {
    this.byteBuffer = packetByteBuffer;
    this.vmInformation = vmInformation;
    this.dataTypeParserMap = new HashMap<>();

    dataTypeParserMap.put(ArrayId.class, ArrayId::new);
    dataTypeParserMap.put(ArrayTypeId.class, ArrayTypeId::new);
    dataTypeParserMap.put(ClassId.class, ClassId::new);
    dataTypeParserMap.put(ClassLoaderId.class, ClassLoaderId::new);
    dataTypeParserMap.put(ClassObjectId.class, ClassObjectId::new);
    dataTypeParserMap.put(EventKind.class, reader -> EventKind.findByValue(reader.readByte()));
    dataTypeParserMap.put(FieldId.class, FieldId::new);
    dataTypeParserMap.put(FrameId.class, FrameId::new);
    dataTypeParserMap.put(IdSizes.class, IdSizes::new);
    dataTypeParserMap.put(InterfaceId.class, InterfaceId::new);
    dataTypeParserMap.put(Location.class, Location::new);
    dataTypeParserMap.put(MethodId.class, MethodId::new);
    dataTypeParserMap.put(ObjectId.class, ObjectId::new);
    dataTypeParserMap.put(ReferenceTypeId.class, ReferenceTypeId::new);
    dataTypeParserMap.put(StringId.class, StringId::new);
    dataTypeParserMap.put(TaggedObjectId.class, TaggedObjectId::new);
    dataTypeParserMap.put(ThreadGroupId.class, ThreadGroupId::new);
    dataTypeParserMap.put(ThreadId.class, ThreadId::new);
    dataTypeParserMap.put(Type.class, reader -> Type.findByValue(reader.readByte()));
//    dataTypeParserMap.put(UntaggedValue.class, UntaggedValue::new);
    dataTypeParserMap.put(Value.class, Value::new);
  }

  public byte readByte() {
    return byteBuffer.get();
  }

  public short readShort() {
    return byteBuffer.getShort();
  }

  public int readInt() {
    return byteBuffer.getInt();
  }

  public long readLong() {
    return byteBuffer.getLong();
  }

  public long readLongOfSize(int size) {
    return ByteBufferUtil.getLong(byteBuffer, size);
  }

  public boolean readBoolean() {
    return byteBuffer.get() != 0;
  }

  public String readString() {
    return ByteBufferUtil.getString(byteBuffer);
  }

  public IdSizes getIdSizes() {
    return vmInformation.getIdSizes();
  }

  @SuppressWarnings("unchecked")
  public <T extends DataType> T readType(Class<T> clazz) {
    return (T) dataTypeParserMap.get(clazz).apply(this);
  }
}
