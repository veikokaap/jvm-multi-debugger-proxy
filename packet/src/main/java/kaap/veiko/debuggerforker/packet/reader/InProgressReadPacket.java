package kaap.veiko.debuggerforker.packet.reader;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import org.checkerframework.checker.nullness.qual.EnsuresNonNullIf;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

class InProgressReadPacket {
    private final ByteBuffer lengthBuffer = ByteBuffer.allocate(4);
    private @MonotonicNonNull ByteBuffer packetBuffer = null;

    void read(SocketChannel socketChannel) throws IOException {
      if (packetBuffer == null) {
        readLength(socketChannel);
      }
      readData(socketChannel);
    }

    private void readLength(SocketChannel socketChannel) throws IOException {
      while (lengthBuffer.hasRemaining()) {
        int read = socketChannel.read(lengthBuffer);
        if (read == 0) {
          return;
        }
      }

      lengthBuffer.flip();
      int packetLength = lengthBuffer.getInt();
      packetBuffer = ByteBuffer.allocate(packetLength - 4);
    }

    private void readData(SocketChannel socketChannel) throws IOException {
      if (packetBuffer == null) {
        return;
      }

      while (packetBuffer.hasRemaining()) {
        int read = socketChannel.read(packetBuffer);
        if (read == 0) {
          return;
        }
      }
    }

    @EnsuresNonNullIf(expression = "this.packetBuffer", result = true)
    private boolean hasFinishedReading() {
      return packetBuffer != null && !packetBuffer.hasRemaining();
    }

    byte @Nullable [] getBytesIfFinishedReading() {
      if (hasFinishedReading()) {
        byte[] array = new byte[packetBuffer.capacity()];
        packetBuffer.flip();
        packetBuffer.get(array);
        return array;
      }
      else {
        return null;
      }
    }
  }