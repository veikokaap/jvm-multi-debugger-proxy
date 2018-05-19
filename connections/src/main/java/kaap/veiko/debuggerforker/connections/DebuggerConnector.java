package kaap.veiko.debuggerforker.connections;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.function.Consumer;

import org.checkerframework.checker.nullness.qual.Nullable;

import kaap.veiko.debuggerforker.packet.DebuggerPacketStream;
import kaap.veiko.debuggerforker.packet.internal.DebuggerPacketTransformer;
import kaap.veiko.debuggerforker.packet.internal.PacketTransformer;

public class DebuggerConnector extends ConnectorBase<DebuggerPacketStream> {
  private final ServerSocketChannel serverChannel;
  private final PacketTransformer packetTransformer = new DebuggerPacketTransformer();

  public static DebuggerConnector create(int port, Consumer<DebuggerPacketStream> listener) throws IOException {
    ServerSocketChannel serverChannel = ServerSocketChannel.open();
    serverChannel.bind(new InetSocketAddress("127.0.0.1", port));
    return new DebuggerConnector(serverChannel, listener);
  }

  private DebuggerConnector(ServerSocketChannel serverChannel, Consumer<DebuggerPacketStream> listener) {
    super(listener, Integer.MAX_VALUE, "DebuggerConnectorThread");
    this.serverChannel = serverChannel;
  }

  @Override
  protected @Nullable DebuggerPacketStream getConnectionBlocking() throws IOException {
    SocketChannel socketChannel = serverChannel.accept();
    socketChannel.configureBlocking(true);
    if (!socketChannel.finishConnect()) {
      throw new IOException("Failed to connect channel");
    }
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
}
