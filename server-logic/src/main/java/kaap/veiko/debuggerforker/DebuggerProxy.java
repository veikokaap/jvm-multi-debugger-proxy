package kaap.veiko.debuggerforker;

import java.io.IOException;
import java.net.InetSocketAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaap.veiko.debuggerforker.commands.Command;
import kaap.veiko.debuggerforker.commands.commandsets.virtualmachine.ResumeCommand;
import kaap.veiko.debuggerforker.handlers.CommandHandler;

public class DebuggerProxy {

  private final static Logger log = LoggerFactory.getLogger(DebuggerProxy.class);

  private final CommandHandler commandHandler;
  private final DebugProxyServer proxyServer;

  private int debuggerCountBeforeResume = 0;
  private int debuggerCount = 0;
  private boolean vmConnected = false;

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
    proxyServer.addVirtualMachineConnectedListener(vmStream -> vmConnected = true);
    proxyServer.addDebuggerConnectedListener(debuggerStream -> {
      debuggerCount += 1;
      if (vmConnected && debuggerCountBeforeResume <= debuggerCount) {
        proxyServer.getProxyCommandStream().writeToVm(ResumeCommand.create(debuggerStream.getSource().createNewOutputId()));
      }
    });

    proxyServer.start();

    while (proxyServer.getProxyCommandStream().isOpen()) {
      Command command = proxyServer.getProxyCommandStream().read();
      if (command != null) {
        command.visit(commandHandler);
      }
    }
  }

  public void setDebuggerCountBeforeResume(int count) {
    this.debuggerCountBeforeResume = count;
  }

  public void stop() {
    proxyServer.close();
  }

}
