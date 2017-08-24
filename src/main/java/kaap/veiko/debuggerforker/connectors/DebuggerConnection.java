package kaap.veiko.debuggerforker.connectors;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class DebuggerConnection extends Connection {
    DebuggerConnection(SocketChannel socketChannel) throws IOException {
        super(socketChannel, false);
    }
}
