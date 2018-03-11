package kaap.veiko.debuggerforker;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaap.veiko.debuggerforker.commands.Command;
import kaap.veiko.debuggerforker.handlers.CommandHandler;

public class DebuggerProxy {

  private final static Logger log = LoggerFactory.getLogger(DebuggerProxy.class);

  private final CommandHandler commandHandler;
  public final DebugProxyServer proxyServer;
  private AtomicBoolean debuggerConnected = new AtomicBoolean(false);
  private final AtomicBoolean vmConnected = new AtomicBoolean(false);

  public DebuggerProxy(InetSocketAddress vmAddress, int debuggerPort) throws IOException {
    proxyServer = new DebugProxyServer(vmAddress, debuggerPort);
    commandHandler = new CommandHandler(proxyServer.getVmInformation(), proxyServer.getProxyCommandStream());
  }

  public void start() {
    proxyServer.addVirtualMachineConnectedListener(vmStream -> vmConnected.set(true));
    proxyServer.addDebuggerConnectedListener(debuggerStream -> debuggerConnected.set(true));

    proxyServer.start();
    waitUntilConnected();

    while (proxyServer.getProxyCommandStream().isOpen()) {
      Command command = proxyServer.getProxyCommandStream().read();
      if (command != null) {
        command.visit(commandHandler);
      }
    }
  }

  private void waitUntilConnected() {
    while (!vmConnected.get() || !debuggerConnected.get()) {
      try {
        Thread.sleep(10);
      }
      catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  public void stop() {
    proxyServer.close();
  }

}
