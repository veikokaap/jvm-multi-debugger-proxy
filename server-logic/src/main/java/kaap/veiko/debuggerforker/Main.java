package kaap.veiko.debuggerforker;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
  public static void main(String[] args) throws IOException {
    InetSocketAddress virtualMachineAddress = new InetSocketAddress("127.0.0.1", 5123);
    DebuggerProxy proxy = new DebuggerProxy(virtualMachineAddress, 5456);
    proxy.start();
  }
}
