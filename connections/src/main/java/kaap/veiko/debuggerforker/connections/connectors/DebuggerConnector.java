package kaap.veiko.debuggerforker.connections.connectors;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

import kaap.veiko.debuggerforker.connections.DebuggerManager;
import kaap.veiko.debuggerforker.packet.PacketIdTransformer;

public class DebuggerConnector implements AutoCloseable {
  private final ServerSocketChannel serverChannel;
  private final PacketIdTransformer packetIdTransformer = new PacketIdTransformer();

  public DebuggerConnector(int port) throws IOException {
    this.serverChannel = ServerSocketChannel.open();
    serverChannel.socket().bind(new InetSocketAddress("127.0.0.1", port));
  }

  public DebuggerManager getConnectionBlocking() throws IOException {
    SocketChannel socketChannel = serverChannel.accept();
    handshake(socketChannel);
    return new DebuggerManager(socketChannel, packetIdTransformer);
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
    serverChannel.close();
  }
}
