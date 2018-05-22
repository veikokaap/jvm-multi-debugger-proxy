package kaap.veiko.debuggerforker.packet.internal;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaap.veiko.debuggerforker.packet.utils.ByteBufferUtil;
import kaap.veiko.debuggerforker.types.DataReadException;
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
  public byte readByte() throws DataReadException {
    try {
      return byteBuffer.get();
    }
    catch (BufferUnderflowException e) {
      throw new DataReadException(e);
    }
  }

  @Override
  public short readShort() throws DataReadException {
    try {
      return byteBuffer.getShort();
    }
    catch (BufferUnderflowException e) {
      throw new DataReadException(e);
    }
  }

  @Override
  public char readChar() throws DataReadException {
    try {
      return byteBuffer.getChar();
    }
    catch (BufferUnderflowException e) {
      throw new DataReadException(e);
    }
  }

  @Override
  public int readInt() throws DataReadException {
    try {
      return byteBuffer.getInt();
    }
    catch (BufferUnderflowException e) {
      throw new DataReadException(e);
    }
  }

  @Override
  public long readLong() throws DataReadException {
    try {
      return byteBuffer.getLong();
    }
    catch (BufferUnderflowException e) {
      throw new DataReadException(e);
    }
  }

  @Override
  public float readFloat() throws DataReadException {
    try {
      return byteBuffer.getFloat();
    }
    catch (BufferUnderflowException e) {
      throw new DataReadException(e);
    }
  }

  @Override
  public double readDouble() throws DataReadException {
    try {
      return byteBuffer.getDouble();
    }
    catch (BufferUnderflowException e) {
      throw new DataReadException(e);
    }
  }

  @Override
  public long readLongOfSize(IdSizes.SizeType sizeType) throws DataReadException {
    IdSizes idSizes = vmInformation.getIdSizes();
    int size;
    if (idSizes == null) {
      log.warn("Parsing value without knowing its size in bytes. Assuming size is 8 bytes.");
      size = 8;
    }
    else {
      size = idSizes.getSizeOfType(sizeType);
    }
    try {
      return ByteBufferUtil.getLong(byteBuffer, size);
    }
    catch (BufferUnderflowException e) {
      throw new DataReadException(e);
    }
  }

  @Override
  public boolean readBoolean() throws DataReadException {
    try {
      return byteBuffer.get() != 0;
    }
    catch (BufferUnderflowException e) {
      throw new DataReadException(e);
    }
  }

  @Override
  public String readString() throws DataReadException {
    try {
      return ByteBufferUtil.getString(byteBuffer);
    }
    catch (BufferUnderflowException | IndexOutOfBoundsException e) {
      throw new DataReadException(e);
    }
  }

}
