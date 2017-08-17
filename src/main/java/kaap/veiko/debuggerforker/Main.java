package kaap.veiko.debuggerforker;

import kaap.veiko.debuggerforker.connectors.DebuggerConnector;

import java.io.IOException;

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
