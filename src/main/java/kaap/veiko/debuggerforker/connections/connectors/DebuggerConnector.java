package kaap.veiko.debuggerforker.connections.connectors;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

import kaap.veiko.debuggerforker.connections.DebuggerConnection;

public class DebuggerConnector {
  private final ServerSocketChannel serverChannel;

  private DebuggerConnector(int port) throws IOException {
    this.serverChannel = ServerSocketChannel.open();
    serverChannel.socket().bind(new InetSocketAddress("127.0.0.1", port));
  }

  public static DebuggerConnection waitForConnectionFromDebugger(int port) throws IOException {
    try {
      DebuggerConnector connector = new DebuggerConnector(port);
      return connector.waitForConnection();
    }
    catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private DebuggerConnection waitForConnection() throws IOException {
    SocketChannel socketChannel = serverChannel.accept();
    handshake(socketChannel);
    return new DebuggerConnection(socketChannel);
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
}
