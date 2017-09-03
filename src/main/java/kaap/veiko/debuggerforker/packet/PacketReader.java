package kaap.veiko.debuggerforker.packet;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

class PacketReader {

  private final SocketChannel socketChannel;

  PacketReader(SocketChannel socketChannel) {
    this.socketChannel = socketChannel;
  }

  Packet read() throws IOException {
    Integer length = readLength();

    if (length == null) {
      return null;
    }

    try {
      byte[] packetBytes = readBytes(length - 4, 100, ByteBuffer::array);
      return PacketParser.parse(length, packetBytes);
    }
    catch (TimeoutException e) {
      throw new IOException("Timeout while reading packet", e);
    }
  }

  private Integer readLength() throws IOException {
    ByteBuffer buf = ByteBuffer.allocate(4);
    while (buf.hasRemaining()) {
      socketChannel.read(buf);
      if (buf.remaining() == 4) {
        return null;
      }
    }
    buf.flip();

    return buf.getInt();
  }

  private <T> T readBytes(int nrOfBytes, long timeoutInMilliseconds, Function<ByteBuffer, T> function) throws IOException, TimeoutException {
    ByteBuffer buf = ByteBuffer.allocate(nrOfBytes);
    long start = System.currentTimeMillis();
    while (buf.hasRemaining()) {
      socketChannel.read(buf);
      if (timeoutExceeded(start, timeoutInMilliseconds)) {
        if (buf.remaining() == nrOfBytes) {
          throw new TimeoutException();
        }
        else {
          throw new IOException("Timeout while reading packet");
        }
      }
    }
    buf.flip();

    return function.apply(buf);
  }

  private boolean timeoutExceeded(long start, long timeoutInMilliseconds) {
    return (System.currentTimeMillis() - start) >= timeoutInMilliseconds;
  }
}
