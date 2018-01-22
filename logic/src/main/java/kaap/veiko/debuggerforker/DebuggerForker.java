package kaap.veiko.debuggerforker;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Selector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaap.veiko.debuggerforker.commands.Command;
import kaap.veiko.debuggerforker.commands.CommandStream;
import kaap.veiko.debuggerforker.connections.DebuggerConnector;
import kaap.veiko.debuggerforker.connections.VMConnector;
import kaap.veiko.debuggerforker.packet.VirtualMachinePacketStream;
import kaap.veiko.debuggerforker.types.VMInformation;

public class DebuggerForker {

  private final static Logger log = LoggerFactory.getLogger(DebuggerForker.class);

  private final CommandStream vmCommandStream;
  private final VMInformation vmInformation = new VMInformation();
  private final ProxyCommandStream proxyCommandStream;

  private final CommandHandler commandHandler;
  private final DebuggerConnector debuggerConnector;

  public static DebuggerForker create(InetSocketAddress virtualMachineAddress, int debuggerPort) throws IOException, InterruptedException {
    VirtualMachinePacketStream vmPacketStream = VMConnector.connectToVM(virtualMachineAddress);
    ProxyCommandStream proxyCommandStream = ProxyCommandStream.create();

    return new DebuggerForker(vmPacketStream, debuggerPort, proxyCommandStream);
  }

  private DebuggerForker(VirtualMachinePacketStream vmPacketStream, int debuggerPort, ProxyCommandStream proxyCommandStream) throws IOException {
    this.vmCommandStream = new CommandStream(vmPacketStream, vmInformation);
    this.commandHandler = new CommandHandler(vmInformation, vmCommandStream, proxyCommandStream);
    this.debuggerConnector = DebuggerConnector.open(debuggerPort, ps -> proxyCommandStream.addCommandStream(new CommandStream(ps, vmInformation)));

    this.proxyCommandStream = proxyCommandStream;
  }

  public void start() throws IOException {
    proxyCommandStream.start();
    debuggerConnector.start();

    CommandStreamChannelSelectorRunnable vmCommandStreamRunnable = CommandStreamChannelSelectorRunnable.create(this::handleCommandReadFromVm, cs -> writeToVm());
    vmCommandStreamRunnable.addCommandStream(vmCommandStream);
    vmCommandStreamRunnable.run();
  }

  private void handleCommandReadFromVm(Command command) {
    command.visit(commandHandler);
  }

  private Command writeToVm() {
    Command command = proxyCommandStream.read();
    if (command != null) {
      command.visit(commandHandler);
    }
    return null;
  }

}
