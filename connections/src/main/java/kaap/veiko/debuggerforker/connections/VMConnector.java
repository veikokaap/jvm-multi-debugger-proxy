package kaap.veiko.debuggerforker.connections;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.function.Consumer;

import kaap.veiko.debuggerforker.packet.VirtualMachinePacketStream;

public class VMConnector extends ConnectorBase<VirtualMachinePacketStream> {
  
  private final InetSocketAddress socketAddress;

  public static VMConnector create(InetSocketAddress socketAddress, Consumer<VirtualMachinePacketStream> listener) {
    return new VMConnector(socketAddress, listener);
  }

  private VMConnector(InetSocketAddress socketAddress, Consumer<VirtualMachinePacketStream> listener) {
    super(listener, 1, "VmConnectorThread");
    this.socketAddress = socketAddress;
  }

  @Override
  protected VirtualMachinePacketStream getConnectionBlocking() throws IOException {
    SocketChannel socketChannel = connect(socketAddress);
    if (socketChannel == null) {
      return null;
    }
    
    handshake(socketChannel);
    return new VirtualMachinePacketStream(socketChannel);
  }

  private SocketChannel connect(InetSocketAddress address) throws IOException {
    try {
      SocketChannel channel = SocketChannel.open(address);
      channel.configureBlocking(true);
      if (!channel.finishConnect()) {
        throw new IOException("Failed to connect channel");
      }
      return channel;
    }
    catch (ConnectException connectException) {
      try {
        Thread.sleep(50);
        return null;
      }
      catch (InterruptedException interruptedException) {
        Thread.currentThread().interrupt();
        throw new IOException("Connection failed - InterruptedException before connected to the VM");
      }
    }
  }

  private void handshake(SocketChannel socketChannel) throws IOException {
    byte[] handshakeBytes = "JDWP-Handshake".getBytes("UTF-8");

    ByteBuffer outBuffer = ByteBuffer.wrap(handshakeBytes);
    while (outBuffer.hasRemaining()) {
      socketChannel.write(outBuffer);
    }

    ByteBuffer inBuffer = ByteBuffer.allocate(handshakeBytes.length);
    int bytesRead = 0;
    while (bytesRead != -1 && inBuffer.hasRemaining()) {
      bytesRead = socketChannel.read(inBuffer);
    }
    byte[] receivedBytes = inBuffer.array();

    if (!Arrays.equals(handshakeBytes, receivedBytes)) {
      throw new IOException("Handshake failed - wrong message from VM");
    }
  }

}
