package kaap.veiko.debuggerforker;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaap.veiko.debuggerforker.commands.CommandStream;
import kaap.veiko.debuggerforker.connections.DebuggerConnector;
import kaap.veiko.debuggerforker.connections.VMConnector;
import kaap.veiko.debuggerforker.packet.DebuggerPacketStream;
import kaap.veiko.debuggerforker.packet.VirtualMachinePacketStream;
import kaap.veiko.debuggerforker.types.VMInformation;

public class DebugProxyServer implements AutoCloseable {
  private static final Logger log = LoggerFactory.getLogger(DebugProxyServer.class);

  private final VMInformation vmInformation = new VMInformation();

  private final ProxyCommandStream proxyCommandStream;
  private final DebuggerConnector debuggerConnector;
  private final VMConnector vmConnector;

  private final List<Consumer<CommandStream>> debuggerConnectListeners = new CopyOnWriteArrayList<>();
  private final List<Consumer<CommandStream>> vmConnectListeners = new CopyOnWriteArrayList<>();

  @SuppressWarnings("methodref.receiver.bound.invalid") // TODO: Using methods under initialization in thread runnable
  public DebugProxyServer(InetSocketAddress virtualMachineAddress, int debuggerPort) throws IOException {
    proxyCommandStream = ProxyCommandStream.create();
    debuggerConnector = DebuggerConnector.create(debuggerPort, this::registerDebuggerStream);
    vmConnector = VMConnector.create(virtualMachineAddress, this::registerVmStream);
  }

  public void addDebuggerConnectedListener(Consumer<CommandStream> listener) {
    debuggerConnectListeners.add(listener);
  }

  public void addVirtualMachineConnectedListener(Consumer<CommandStream> listener) {
    vmConnectListeners.add(listener);
  }

  public ProxyCommandStream getProxyCommandStream() {
    return proxyCommandStream;
  }

  private void registerDebuggerStream(DebuggerPacketStream debuggerPacketStream) {
    CommandStream commandStream = new CommandStream(debuggerPacketStream, vmInformation);
    debuggerConnectListeners.forEach(listener -> listener.accept(commandStream));
    registerStream(commandStream);
  }

  private void registerVmStream(VirtualMachinePacketStream vmPacketStream) {
    CommandStream commandStream = new CommandStream(vmPacketStream, vmInformation);
    vmConnectListeners.forEach(listener -> listener.accept(commandStream));
    registerStream(commandStream);
  }

  private void registerStream(CommandStream commandStream) {
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

  public void start() {
    vmConnector.start();
    debuggerConnector.start();
    proxyCommandStream.start();
  }

  public void close() {
    vmConnector.close();
    debuggerConnector.close();
    proxyCommandStream.close();
  }

  public VMInformation getVmInformation() {
    return vmInformation;
  }
}
