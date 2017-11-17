package kaap.veiko.debuggerforker.packet;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import kaap.veiko.debuggerforker.packet.utils.ByteBufferUtil;
import kaap.veiko.debuggerforker.types.DataReader;
import kaap.veiko.debuggerforker.types.DataType;
import kaap.veiko.debuggerforker.types.VMInformation;
import kaap.veiko.debuggerforker.types.jdwp.ArrayId;
import kaap.veiko.debuggerforker.types.jdwp.ArrayTypeId;
import kaap.veiko.debuggerforker.types.jdwp.ClassId;
import kaap.veiko.debuggerforker.types.jdwp.ClassLoaderId;
import kaap.veiko.debuggerforker.types.jdwp.ClassObjectId;
import kaap.veiko.debuggerforker.types.jdwp.EventKind;
import kaap.veiko.debuggerforker.types.jdwp.FieldId;
import kaap.veiko.debuggerforker.types.jdwp.FrameId;
import kaap.veiko.debuggerforker.types.jdwp.IdSizes;
import kaap.veiko.debuggerforker.types.jdwp.InterfaceId;
import kaap.veiko.debuggerforker.types.jdwp.Location;
import kaap.veiko.debuggerforker.types.jdwp.MethodId;
import kaap.veiko.debuggerforker.types.jdwp.ObjectId;
import kaap.veiko.debuggerforker.types.jdwp.ReferenceTypeId;
import kaap.veiko.debuggerforker.types.jdwp.StringId;
import kaap.veiko.debuggerforker.types.jdwp.TaggedObjectId;
import kaap.veiko.debuggerforker.types.jdwp.ThreadGroupId;
import kaap.veiko.debuggerforker.types.jdwp.ThreadId;
import kaap.veiko.debuggerforker.types.jdwp.Type;
import kaap.veiko.debuggerforker.types.jdwp.Value;


public class PacketDataReader implements DataReader {
  private final ByteBuffer byteBuffer;
  private final VMInformation vmInformation;
  private final Map<Class<? extends DataType>, Function<DataReader, ? extends DataType>> dataTypeParserMap;

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
//    dataTypeParserMap.put(UntaggedValue.class, reader -> new UntaggedValue(reader));
    dataTypeParserMap.put(Value.class, Value::new);
  }

  @Override
  public byte readByte() {
    return byteBuffer.get();
  }

  @Override
  public short readShort() {
    return byteBuffer.getShort();
  }

  @Override
  public int readInt() {
    return byteBuffer.getInt();
  }

  @Override
  public long readLong() {
    return byteBuffer.getLong();
  }

  @Override
  public long readLongOfSize(int size) {
    return ByteBufferUtil.getLong(byteBuffer, size);
  }

  @Override
  public boolean readBoolean() {
    return byteBuffer.get() != 0;
  }

  @Override
  public String readString() {
    return ByteBufferUtil.getString(byteBuffer);
  }

  @Override
  public IdSizes getIdSizes() {
    return vmInformation.getIdSizes();
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T extends DataType> T readType(Class<T> clazz) {
    return (T) dataTypeParserMap.get(clazz).apply(this);
  }
}
