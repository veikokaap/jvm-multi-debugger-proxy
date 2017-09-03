package kaap.veiko.debuggerforker.connections.connectors;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

import kaap.veiko.debuggerforker.connections.VirtualMachineConnection;

public class VMConnector {

  public static VirtualMachineConnection connectToVM(InetSocketAddress address) throws InterruptedException, IOException {
    SocketChannel socketChannel = new VMConnector().connectAndHandshake(address);
    return new VirtualMachineConnection(socketChannel);
  }

  private SocketChannel connectAndHandshake(InetSocketAddress address) throws InterruptedException, IOException {
    SocketChannel socketChannel = connect(address);
    handshake(socketChannel);
    return socketChannel;
  }

  private SocketChannel connect(InetSocketAddress address) throws IOException {
    while (true) {
      try {
        return SocketChannel.open(address);
      }
      catch (ConnectException connectException) {
        try {
          Thread.sleep(1000);
        }
        catch (InterruptedException interruptedException) {
          Thread.currentThread().interrupt();
          throw new IOException("Connection failed - InterruptedException before connected to the VM");
        }
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
