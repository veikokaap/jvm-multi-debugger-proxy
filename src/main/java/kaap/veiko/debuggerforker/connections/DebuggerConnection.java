package kaap.veiko.debuggerforker.connections;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class DebuggerConnection extends Connection {
    public DebuggerConnection(SocketChannel socketChannel) throws IOException {
        super(socketChannel, false);
    }
}
