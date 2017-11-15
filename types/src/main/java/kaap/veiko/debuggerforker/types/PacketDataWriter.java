package kaap.veiko.debuggerforker.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.utils.ByteBufferUtil;

public class PacketDataWriter {

  private final ByteBuffer byteBuffer;
  private final VMInformation vmInformation;

  public PacketDataWriter(ByteBuffer byteBuffer, VMInformation vmInformation) {
    this.byteBuffer = byteBuffer;
    this.vmInformation = vmInformation;
  }

  public void writeBoolean(boolean bool) {
    if (bool) {
      byteBuffer.put((byte) 1);
    }
    else {
      byteBuffer.put((byte) 0);
    }
  }

  public void writeByte(byte b) {
    byteBuffer.put(b);
  }

  public void writeShort(short s) {
    byteBuffer.putShort(s);
  }

  public void writeInt(int i) {
    byteBuffer.putInt(i);
  }

  public void writeLong(long l) {
    byteBuffer.putLong(l);
  }

  public void writeLongOfSize(long l, int size) {
    ByteBufferUtil.putLong(byteBuffer, l, size);
  }

  public void writeType(DataType type) {
    type.write(this);
  }
}
