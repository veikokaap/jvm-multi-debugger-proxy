package kaap.veiko.debuggerforker;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicBoolean;
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

  private final AtomicBoolean running = new AtomicBoolean(true);

  public static DebuggerForker start(InetSocketAddress virtualMachineAddress, int debuggerPort) throws IOException {
    ProxyCommandStream proxyCommandStream = ProxyCommandStream.create();
    DebuggerForker debuggerForker = new DebuggerForker(virtualMachineAddress, debuggerPort, proxyCommandStream);
    debuggerForker.start();

    return debuggerForker;
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

  private void start() {
    proxyCommandStream.start();
    debuggerConnector.start();
    vmConnector.start();

    while (running.get()) {
      Command command = proxyCommandStream.read();
      if (command != null) {
        command.visit(commandHandler);
      }
    }
  }

  public void stop() {
    running.set(false);
  }


}
