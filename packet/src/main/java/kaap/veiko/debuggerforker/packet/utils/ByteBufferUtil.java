package kaap.veiko.debuggerforker.packet.utils;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class ByteBufferUtil {
  public static Long getLong(ByteBuffer buffer, int size) {
    if (size > 8 || size < 0) {
      throw new UnsupportedOperationException("malformed input");
    }
    int offset = 8 - size;
    byte[] bytes = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};
    buffer.get(bytes, offset, size);
    return ByteBuffer.wrap(bytes).getLong();
  }

  public static void putLong(ByteBuffer buffer, long value, int size) {
    ByteBuffer tmpBuffer = ByteBuffer.allocate(8);
    tmpBuffer.putLong(value);
    tmpBuffer.flip();

    byte[] bytes = new byte[size];
    for (int i = 0; i < (8 - size); i++) {
      tmpBuffer.get();
    }
    tmpBuffer.get(bytes, 0, size);

    buffer.put(bytes);
  }

  public static Integer getInt(ByteBuffer buffer, int size) {
    if (size > 4 || size < 0) {
      throw new UnsupportedOperationException("malformed input");
    }
    int offset = 4 - size;
    byte[] bytes = new byte[]{0, 0, 0, 0};
    buffer.get(bytes, offset, size);
    return ByteBuffer.wrap(bytes).getInt();
  }

  public static Short getShort(ByteBuffer buffer, int size) {
    if (size > 2 || size < 0) {
      throw new UnsupportedOperationException("malformed input");
    }
    int offset = 2 - size;
    byte[] bytes = new byte[]{0, 0};
    buffer.get(bytes, offset, size);
    return ByteBuffer.wrap(bytes).getShort();
  }

  public static String getString(ByteBuffer buffer) {
    int length = buffer.getInt();
    byte[] bytes = new byte[length];
    buffer.get(bytes, 0, length);

    return new String(bytes, StandardCharsets.UTF_8);
  }

  public static void putString(ByteBuffer buffer, String string) {
    byte[] bytes = string.getBytes(StandardCharsets.UTF_8);

    buffer.putInt(bytes.length);
    buffer.put(bytes);
  }
}
