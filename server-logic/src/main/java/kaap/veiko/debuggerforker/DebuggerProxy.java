package kaap.veiko.debuggerforker;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaap.veiko.debuggerforker.commands.Command;
import kaap.veiko.debuggerforker.connections.DebuggerConnector;
import kaap.veiko.debuggerforker.connections.VMConnector;
import kaap.veiko.debuggerforker.handlers.CommandHandler;
import kaap.veiko.debuggerforker.types.VMInformation;

public class DebuggerProxy {

  private final static Logger log = LoggerFactory.getLogger(DebuggerProxy.class);

  private final CommandHandler commandHandler;
  private final DebugProxyServer proxyServer;

  public static DebuggerProxy start(InetSocketAddress virtualMachineAddress, int debuggerPort) throws IOException {
    DebuggerProxy debuggerProxy = new DebuggerProxy(virtualMachineAddress, debuggerPort);
    debuggerProxy.start();
    return debuggerProxy;
  }

  private DebuggerProxy(InetSocketAddress vmAddress, int debuggerPort) throws IOException {
    proxyServer = new DebugProxyServer(vmAddress, debuggerPort);
    commandHandler = new CommandHandler(proxyServer.getVmInformation(), proxyServer.getProxyCommandStream());
  }

  private void start() {
    proxyServer.start();

    while (proxyServer.getProxyCommandStream().isOpen()) {
      Command command = proxyServer.getProxyCommandStream().read();
      if (command != null) {
        command.visit(commandHandler);
      }
    }
  }

  public void stop() {
    proxyServer.close();
  }

}
