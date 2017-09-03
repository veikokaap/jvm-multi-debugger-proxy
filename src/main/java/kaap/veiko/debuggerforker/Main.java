package kaap.veiko.debuggerforker;


import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
  public static void main(String[] args) throws IOException, InterruptedException {
    InetSocketAddress virtualMachineAddress = new InetSocketAddress("127.0.0.1", 5123);
    DebuggerForker.start(virtualMachineAddress, 5456);
  }
}
