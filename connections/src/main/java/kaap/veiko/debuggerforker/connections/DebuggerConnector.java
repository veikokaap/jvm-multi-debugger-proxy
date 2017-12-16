package kaap.veiko.debuggerforker.connections;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

import kaap.veiko.debuggerforker.packet.DebuggerPacketStream;
import kaap.veiko.debuggerforker.packet.internal.PacketTransformer;

public class DebuggerConnector implements AutoCloseable {
  private final ServerSocketChannel serverChannel;
  private final PacketTransformer packetTransformer = new PacketTransformer();

  public DebuggerConnector(int port) throws IOException {
    this.serverChannel = ServerSocketChannel.open();
    serverChannel.bind(new InetSocketAddress("127.0.0.1", port));
  }

  public DebuggerPacketStream getConnectionBlocking() throws IOException {
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
    serverChannel.close();
  }
}
