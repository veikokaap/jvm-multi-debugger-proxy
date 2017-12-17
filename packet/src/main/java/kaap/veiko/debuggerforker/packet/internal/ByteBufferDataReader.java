package kaap.veiko.debuggerforker.packet.internal;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaap.veiko.debuggerforker.packet.utils.ByteBufferUtil;
import kaap.veiko.debuggerforker.types.DataReader;

import kaap.veiko.debuggerforker.types.VMInformation;
import kaap.veiko.debuggerforker.types.jdwp.IdSizes;

public class ByteBufferDataReader implements DataReader {

  private final static Logger log = LoggerFactory.getLogger(ByteBufferDataReader.class);

  private final ByteBuffer byteBuffer;
  private final VMInformation vmInformation;

  public ByteBufferDataReader(ByteBuffer packetByteBuffer, VMInformation vmInformation) {
    this.byteBuffer = packetByteBuffer;
    this.vmInformation = vmInformation;
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
  public long readLongOfSize(IdSizes.SizeType sizeType) {
    IdSizes idSizes = vmInformation.getIdSizes();
    int size;
    if (idSizes == null) {
      log.warn("Parsing value without knowing its size in bytes. Assuming size is 8 bytes.");
      size = 8;
    }
    else {
      size = idSizes.getSizeOfType(sizeType);
    }
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

}
