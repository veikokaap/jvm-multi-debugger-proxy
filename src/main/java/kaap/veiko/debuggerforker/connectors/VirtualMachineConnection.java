package kaap.veiko.debuggerforker.connectors;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class VirtualMachineConnection extends Connection {
    VirtualMachineConnection(SocketChannel socketChannel) throws IOException {
        super(socketChannel, true);
    }
}
