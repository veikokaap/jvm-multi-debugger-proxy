package kaap.veiko.debuggerforker.connectors;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.function.Consumer;

public class DebuggerConnector {
    private final ServerSocketChannel serverChannel;

    public static void listenForSocketChannels(int port, Consumer<SocketChannel> consumer) throws IOException {
        new Thread(() -> {
            try {
                DebuggerConnector connector = new DebuggerConnector(port);
                connector.waitForConnection(consumer);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }).start();
    }

    private DebuggerConnector(int port) throws IOException {
        this.serverChannel = ServerSocketChannel.open();
        serverChannel.socket().bind(new InetSocketAddress("127.0.0.1", port));
    }

    private void waitForConnection(Consumer<SocketChannel> consumer) throws IOException {
        while (!Thread.interrupted()) {
            SocketChannel socketChannel = serverChannel.accept();
            handshake(socketChannel);
            consumer.accept(socketChannel);
        }
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
