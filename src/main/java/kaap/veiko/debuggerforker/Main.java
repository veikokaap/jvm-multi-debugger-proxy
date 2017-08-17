package kaap.veiko.debuggerforker;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
//        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 5123);
//
//        SocketChannel socketChannel = VMConnector.connectToVM(address);
        DebuggerConnector.listenForSocketChannels(5456, sc -> {
            System.out.println(sc);
        });
    }
}
