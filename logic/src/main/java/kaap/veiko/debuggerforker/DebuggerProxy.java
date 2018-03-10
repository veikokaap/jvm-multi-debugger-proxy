package kaap.veiko.debuggerforker;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaap.veiko.debuggerforker.commands.Command;
import kaap.veiko.debuggerforker.commands.CommandStream;
import kaap.veiko.debuggerforker.connections.DebuggerConnector;
import kaap.veiko.debuggerforker.connections.VMConnector;
import kaap.veiko.debuggerforker.handlers.CommandHandler;
import kaap.veiko.debuggerforker.managers.ProxyCommandStream;
import kaap.veiko.debuggerforker.packet.PacketStream;
import kaap.veiko.debuggerforker.types.VMInformation;

public class DebuggerProxy {

  private final static Logger log = LoggerFactory.getLogger(DebuggerProxy.class);

  private final VMInformation vmInformation = new VMInformation();
  private final ProxyCommandStream proxyCommandStream;

  private final CommandHandler commandHandler;
  private final DebuggerConnector debuggerConnector;
  private final VMConnector vmConnector;

  public static DebuggerProxy start(InetSocketAddress virtualMachineAddress, int debuggerPort) throws IOException {
    ProxyCommandStream proxyCommandStream = ProxyCommandStream.create();
    DebuggerProxy debuggerProxy = new DebuggerProxy(virtualMachineAddress, debuggerPort, proxyCommandStream);
    debuggerProxy.start();

    return debuggerProxy;
  }

  private DebuggerProxy(InetSocketAddress vmAddress, int debuggerPort, ProxyCommandStream proxyCommandStream) throws IOException {
    this.commandHandler = new CommandHandler(vmInformation, proxyCommandStream);
    this.debuggerConnector = DebuggerConnector.create(debuggerPort, this::registerStream);
    this.vmConnector = VMConnector.create(vmAddress, ps -> {
      registerStream(ps);
      debuggerConnector.start();
    });

    this.proxyCommandStream = proxyCommandStream;
  }

  private void registerStream(PacketStream debuggerPacketStream) {
    CommandStream commandStream = new CommandStream(debuggerPacketStream, vmInformation);
    try {
      proxyCommandStream.registerCommandStream(commandStream);
    }
    catch (Exception exception) {
      log.error("Exception during register", exception);
      commandStream.close();
      if (!proxyCommandStream.isOpen()) {
        proxyCommandStream.close();
      }
    }
  }

  private void start() {
    proxyCommandStream.start();
    vmConnector.start();

    while (proxyCommandStream.isOpen()) {
      Command command = proxyCommandStream.read();
      if (command != null) {
        command.visit(commandHandler);
      }
    }
  }

  public void stop() {
    proxyCommandStream.close();
  }

}
