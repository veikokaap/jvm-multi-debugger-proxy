package kaap.veiko.debuggerforker.packet.internal;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import kaap.veiko.debuggerforker.packet.Packet;
import kaap.veiko.debuggerforker.packet.PacketStream;

class PacketReader {

  private final SocketChannel socketChannel;
  private final PacketStream stream;

  private boolean readingLength = true;
  private boolean readingData = false;
  private boolean doneReading = false;
  private int length;
  private ByteBuffer lengthBuffer = ByteBuffer.allocate(4);
  private ByteBuffer dataBuffer = null;

  PacketReader(SocketChannel socketChannel, PacketStream stream) {
    this.socketChannel = socketChannel;
    this.stream = stream;
  }

  public Packet read() throws IOException {
    if (readingLength) {
      readLength();
    }
    if (readingData) {
      readData();
    }

    if (doneReading) {
      doneReading = false;
      readingLength = true;
      dataBuffer.flip();
      byte[] array;
      if (dataBuffer.hasArray()) {
        array = dataBuffer.array();
      } else {
        array = new byte[length];
        dataBuffer.get(array);
      }

      return PacketParser.parse(length, array, stream);
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
