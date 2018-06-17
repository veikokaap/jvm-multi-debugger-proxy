package kaap.veiko.debuggerforker.commands.commandsets.event.events;

import static org.junit.Assert.assertEquals;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import kaap.veiko.debuggerforker.commands.commandsets.TestBase;
import kaap.veiko.debuggerforker.packet.reader.ByteBufferDataReader;
import kaap.veiko.debuggerforker.packet.writer.ByteBufferDataWriter;
import kaap.veiko.debuggerforker.packet.utils.ByteBufferUtil;
import kaap.veiko.debuggerforker.types.DataReadException;
import kaap.veiko.debuggerforker.types.DataReader;
import kaap.veiko.debuggerforker.types.jdwp.ArrayId;
import kaap.veiko.debuggerforker.types.jdwp.ClassId;
import kaap.veiko.debuggerforker.types.jdwp.EventKind;
import kaap.veiko.debuggerforker.types.jdwp.FieldId;
import kaap.veiko.debuggerforker.types.jdwp.Location;
import kaap.veiko.debuggerforker.types.jdwp.MethodId;
import kaap.veiko.debuggerforker.types.jdwp.ObjectId;
import kaap.veiko.debuggerforker.types.jdwp.ReferenceTypeId;
import kaap.veiko.debuggerforker.types.jdwp.StringId;
import kaap.veiko.debuggerforker.types.jdwp.TaggedObjectId;
import kaap.veiko.debuggerforker.types.jdwp.ThreadId;
import kaap.veiko.debuggerforker.types.jdwp.Type;
import kaap.veiko.debuggerforker.types.jdwp.Value;

public abstract class EventTestBase extends TestBase {

  private final Random random = new Random();

  protected <T extends VirtualMachineEvent> void assertWrittenEventEqualsReadEvent(EventKind expectedEventKind, T originalEvent) throws ReflectiveOperationException, DataReadException {
    ByteBuffer buffer = ByteBuffer.allocate(4096);

    writeEvent(buffer, originalEvent);
    EventKind readEventKind = EventKind.read(createReader(buffer));
    T readEvent = readEvent(buffer, (Class<T>) originalEvent.getClass());

    assertEquals(expectedEventKind, readEventKind);
    assertEquals(originalEvent, readEvent);
  }

  <T extends VirtualMachineEvent> T readEvent(ByteBuffer buffer, Class<T> dataTypeClass) throws ReflectiveOperationException {
    return (T) dataTypeClass.getMethod("read", DataReader.class).invoke(new Object(), createReader(buffer));
  }

  void writeEvent(ByteBuffer buffer, VirtualMachineEvent event) {
    createWriter(buffer).writeType(event);
    buffer.flip();
  }

  private ByteBufferDataWriter createWriter(ByteBuffer buffer) {
    return new ByteBufferDataWriter(buffer, getVmInformation());
  }

  private DataReader createReader(ByteBuffer buffer) {
    return new ByteBufferDataReader(buffer, getVmInformation());
  }

  ObjectId randomObjectId() {
    return new ObjectId(randomLongOfBytes(getIdSizes().getObjectIdSize()));
  }

  ThreadId randomThreadId() {
    return new ThreadId(randomLongOfBytes(getIdSizes().getObjectIdSize()));
  }

  ClassId randomClassId() {
    return new ClassId(randomLongOfBytes(getIdSizes().getObjectIdSize()));
  }

  MethodId randomMethodId() {
    return new MethodId(randomLongOfBytes(getIdSizes().getMethodIdSize()));
  }

  Location randomLocation() {
    return new Location(randomByte(), randomClassId(), randomMethodId(), random.nextLong());
  }

  ReferenceTypeId randomReferenceTypeId() {
    return new ReferenceTypeId(randomLongOfBytes(getIdSizes().getReferenceTypeIdSize()));
  }

  FieldId randomFieldId() {
    return new FieldId(randomLongOfBytes(getIdSizes().getFieldIdSize()));
  }

  TaggedObjectId randomTaggedObjectId() {
    return new TaggedObjectId(randomByte(), randomObjectId());
  }

  StringId randomStringId() {
    return new StringId(randomLongOfBytes(getIdSizes().getObjectIdSize()));
  }

  ArrayId randomArrayId() {
    return new ArrayId(randomLongOfBytes(getIdSizes().getObjectIdSize()));
  }

  //TODO: Support and test all Value types
  Value randomValue() {
    switch (random.nextInt(5)) {
      case 0:
        return new Value<>(Type.INT, randomInt());
      case 1:
        return new Value<>(Type.ARRAY, randomArrayId());
      case 2:
        return new Value<>(Type.STRING, randomStringId());
      case 3:
        return new Value<>(Type.DOUBLE, random.nextDouble());
      case 4:
        return new Value<>(Type.VOID, null);
      default:
        throw new IllegalStateException("Default switch");
    }
  }

  String randomString() {
    byte[] bytes = new byte[random.nextInt(500) + 5];
    random.nextBytes(bytes);
    return new String(bytes, StandardCharsets.UTF_8);
  }

  private long randomLongOfBytes(int nrOfBytes) {
    byte[] bytes = new byte[nrOfBytes];
    random.nextBytes(bytes);

    ByteBuffer buffer = ByteBuffer.wrap(bytes);
    return ByteBufferUtil.getLong(buffer, nrOfBytes);
  }

  byte randomByte() {
    byte[] bytes = new byte[1];
    random.nextBytes(bytes);
    return bytes[0];
  }

  int randomInt() {
    return random.nextInt();
  }

  long randomLong() {
    return random.nextLong();
  }

  boolean randomBoolean() {
    return random.nextBoolean();
  }
}
