package kaap.veiko.debuggerforker.packet.internal;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaap.veiko.debuggerforker.packet.utils.ByteBufferUtil;
import kaap.veiko.debuggerforker.types.DataType;
import kaap.veiko.debuggerforker.types.DataWriter;
import kaap.veiko.debuggerforker.types.VMInformation;
import kaap.veiko.debuggerforker.types.jdwp.IdSizes;

public class ByteBufferDataWriter implements DataWriter {

  private final static Logger log = LoggerFactory.getLogger(ByteBufferDataWriter.class);

  private final ByteBuffer byteBuffer;
  private final VMInformation vmInformation;

  public ByteBufferDataWriter(ByteBuffer byteBuffer, VMInformation vmInformation) {
    this.byteBuffer = byteBuffer;
    this.vmInformation = vmInformation;
  }

  @Override
  public void writeBoolean(boolean bool) {
    if (bool) {
      byteBuffer.put((byte) 1);
    }
    else {
      byteBuffer.put((byte) 0);
    }
  }

  @Override
  public void writeByte(byte b) {
    byteBuffer.put(b);
  }

  @Override
  public void writeShort(short s) {
    byteBuffer.putShort(s);
  }

  @Override
  public void writeInt(int i) {
    byteBuffer.putInt(i);
  }

  @Override
  public void writeLong(long l) {
    byteBuffer.putLong(l);
  }

  @Override
  public void writeLongOfSize(long l, IdSizes.SizeType sizeType) {
    IdSizes idSizes = vmInformation.getIdSizes();
    int size;
    if (idSizes == null) {
      log.warn("Writing value without knowing its size in bytes. Assuming size is 8 bytes.");
      size = 8;
    }
    else {
      size = idSizes.getSizeOfType(sizeType);
    }

    ByteBufferUtil.putLong(byteBuffer, l, size);
  }

  @Override
  public void writeString(String string) {
    ByteBufferUtil.putString(byteBuffer, string);
  }

  @Override
  public void writeType(DataType type) {
    type.write(this);
  }
}
