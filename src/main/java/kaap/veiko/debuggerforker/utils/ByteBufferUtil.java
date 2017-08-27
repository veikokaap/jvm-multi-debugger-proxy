package kaap.veiko.debuggerforker.utils;

import java.nio.ByteBuffer;

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
}
