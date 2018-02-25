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
import kaap.veiko.debuggerforker.types.VMInformation;

public class DebuggerForker {

  private final static Logger log = LoggerFactory.getLogger(DebuggerForker.class);

  private final VMInformation vmInformation = new VMInformation();
  private final ProxyCommandStream proxyCommandStream;

  private final CommandHandler commandHandler;
  private final DebuggerConnector debuggerConnector;
  private final VMConnector vmConnector;
  
  public static DebuggerForker create(InetSocketAddress virtualMachineAddress, int debuggerPort) throws IOException {
    ProxyCommandStream proxyCommandStream = ProxyCommandStream.create();
    return new DebuggerForker(virtualMachineAddress, debuggerPort, proxyCommandStream);
  }

  private DebuggerForker(InetSocketAddress vmAddress, int debuggerPort, ProxyCommandStream proxyCommandStream) throws IOException {
    this.commandHandler = new CommandHandler(vmInformation, proxyCommandStream);
    this.debuggerConnector = DebuggerConnector.create(debuggerPort, ps ->
        proxyCommandStream.addCommandStream(new CommandStream(ps, vmInformation))
    );
    this.vmConnector = VMConnector.create(vmAddress, ps ->
        proxyCommandStream.addCommandStream(new CommandStream(ps, vmInformation))
    );

    this.proxyCommandStream = proxyCommandStream;
  }

  public void start() {
    proxyCommandStream.start();
    debuggerConnector.start();
    vmConnector.start();

    while (true) {
      Command command = proxyCommandStream.read();
      if (command != null) {
        command.visit(commandHandler);
      }
    }
  }

}
