package kaap.veiko.debuggerforker.packet.internal;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.packet.utils.ByteBufferUtil;
import kaap.veiko.debuggerforker.types.DataType;
import kaap.veiko.debuggerforker.types.DataWriter;
import kaap.veiko.debuggerforker.types.VMInformation;

public class ByteBufferDataWriter implements DataWriter {

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
  public void writeLongOfSize(long l, int size) {
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
