package kaap.veiko.debuggerforker.connections;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.function.Consumer;

import kaap.veiko.debuggerforker.packet.DebuggerPacketStream;
import kaap.veiko.debuggerforker.packet.internal.PacketTransformer;

public class DebuggerConnector implements AutoCloseable {
  private final ServerSocketChannel serverChannel;
  private final PacketTransformer packetTransformer = new PacketTransformer();
  private final Thread thread;

  public static DebuggerConnector open(int port, Consumer<DebuggerPacketStream> listener) throws IOException {
    ServerSocketChannel serverChannel = ServerSocketChannel.open();
    serverChannel.bind(new InetSocketAddress("127.0.0.1", port));
    return new DebuggerConnector(serverChannel, listener);
  }

  private DebuggerConnector(ServerSocketChannel serverChannel, Consumer<DebuggerPacketStream> listener) throws IOException {
    this.serverChannel = serverChannel;
    thread = new Thread(() -> {
      while (!Thread.interrupted()) {
        try {
          DebuggerPacketStream packetStream = getConnectionBlocking();
          listener.accept(packetStream);
        } catch (Exception e) {
          return;
        }
      }
    }, "DebuggerConnectorThread");
  }

  public void start() {
    thread.start();
  }

  private DebuggerPacketStream getConnectionBlocking() throws IOException {
    SocketChannel socketChannel = serverChannel.accept();
    handshake(socketChannel);
    return new DebuggerPacketStream(socketChannel, packetTransformer);
  }

  private void handshake(SocketChannel socketChannel) throws IOException {
    byte[] handshakeBytes = "JDWP-Handshake".getBytes("UTF-8");

    ByteBuffer inBuffer = ByteBuffer.allocate(handshakeBytes.length);
    int bytesRead = 0;
    while (bytesRead != -1 && inBuffer.hasRemaining()) {
      bytesRead = socketChannel.read(inBuffer);
    }
    byte[] receivedBytes = inBuffer.array();

    if (!Arrays.equals(handshakeBytes, receivedBytes)) {
      throw new IOException("Handshake failed - wrong message from Debugger");
    }

    ByteBuffer outBuffer = ByteBuffer.wrap(handshakeBytes);
    while (outBuffer.hasRemaining()) {
      socketChannel.write(outBuffer);
    }
  }

  @Override
  public void close() throws IOException {
    thread.interrupt();
    serverChannel.close();
  }
}
