package kaap.veiko.debuggerforker.packet.internal;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import org.checkerframework.checker.nullness.qual.Nullable;

import kaap.veiko.debuggerforker.packet.Packet;
import kaap.veiko.debuggerforker.packet.PacketSource;

class PacketReader {

  private final SocketChannel socketChannel;
  private final PacketSource source;

  private boolean readingLength = true;
  private boolean readingData = false;
  private boolean doneReading = false;
  private int length;
  private ByteBuffer lengthBuffer = ByteBuffer.allocate(4);
  private @Nullable ByteBuffer dataBuffer = null;

  PacketReader(SocketChannel socketChannel, PacketSource source) {
    this.socketChannel = socketChannel;
    this.source = source;
  }

  public @Nullable Packet read() throws IOException {
    if (readingLength) {
      readLength();
    }
    if (readingData) {
      readData();
    }

    if (doneReading) {
      doneReading = false;
      readingLength = true;
      if (dataBuffer == null) {
        throw new IllegalStateException("Data buffer is null.");
      }
      dataBuffer.flip();
      byte[] array;
      if (dataBuffer.hasArray()) {
        array = dataBuffer.array();
      }
      else {
        array = new byte[length];
        dataBuffer.get(array);
      }

      return PacketParser.parse(length, array, source);

    }
    return null;
  }

  private void readLength() throws IOException {
    while (lengthBuffer.hasRemaining()) {
      int read = socketChannel.read(lengthBuffer);
      if (read == 0) {
        break;
      }
    }

    if (!lengthBuffer.hasRemaining()) {
      lengthBuffer.flip();
      length = lengthBuffer.getInt();
      dataBuffer = ByteBuffer.allocate(length - 4);
      lengthBuffer.clear();
      readingLength = false;
      readingData = true;
    }
  }

  private void readData() throws IOException {
    if (dataBuffer == null) {
      throw new IllegalStateException("Data buffer is null.");
    }
    while (dataBuffer.hasRemaining()) {
      int read = socketChannel.read(dataBuffer);
      if (read == 0) {
        break;
      }
    }

    if (!dataBuffer.hasRemaining()) {
      readingData = false;
      doneReading = true;
    }
  }
}
