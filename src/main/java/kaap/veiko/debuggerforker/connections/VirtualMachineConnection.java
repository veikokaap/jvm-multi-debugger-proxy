package kaap.veiko.debuggerforker.connections;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class VirtualMachineConnection extends Connection {
    public VirtualMachineConnection(SocketChannel socketChannel) throws IOException {
        super(socketChannel, true);
    }
}
