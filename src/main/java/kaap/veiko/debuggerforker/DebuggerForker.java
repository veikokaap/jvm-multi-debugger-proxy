package kaap.veiko.debuggerforker;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaap.veiko.debuggerforker.commands.Command;
import kaap.veiko.debuggerforker.commands.parser.CommandParser;
import kaap.veiko.debuggerforker.commands.sets.virtualmachine.IdSizesReplyCommand;
import kaap.veiko.debuggerforker.connections.DebuggerConnection;
import kaap.veiko.debuggerforker.connections.VirtualMachineConnection;
import kaap.veiko.debuggerforker.connections.connectors.DebuggerConnector;
import kaap.veiko.debuggerforker.connections.connectors.VMConnector;
import kaap.veiko.debuggerforker.packet.Packet;

public class DebuggerForker implements AutoCloseable {

  private final static Logger log = LoggerFactory.getLogger(DebuggerForker.class);

  private final VirtualMachineConnection vm;
  private final VMInformation vmInformation = new VMInformation();
  private final CommandParser commandParser = new CommandParser(vmInformation);
  private final List<DebuggerConnection> debuggers = new ArrayList<>();

  private final Thread debuggerConnectionThread;

  private DebuggerForker(VirtualMachineConnection vm, int debuggerPort) {
    this.vm = vm;

    debuggerConnectionThread = new Thread(() -> {
      try {
        DebuggerConnection debugger = DebuggerConnector.waitForConnectionFromDebugger(debuggerPort);
        synchronized (debuggers) {
          debuggers.add(debugger);
          debuggers.notifyAll();
        }
      }
      catch (IOException e) {
        log.error("Exception while waiting for debuggers to connect", e);
      }
    });
  }

  public static void start(InetSocketAddress virtualMachineAddress, int debuggerPort) throws IOException, InterruptedException {
    VirtualMachineConnection vm = VMConnector.connectToVM(virtualMachineAddress);

    DebuggerForker debuggerForker = new DebuggerForker(vm, debuggerPort);
    debuggerForker.start();
  }

  private void start() throws IOException, InterruptedException {
    debuggerConnectionThread.start();
    waitForFirstDebuggerConnection();

    while (true) {
      Packet vmPacket = vm.getPacketStream().read();

      if (vmPacket != null) {
        log.info("VMachine: {}", vmPacket);
        Command command = commandParser.parse(vmPacket);
        if (command != null) {
          log.info("Parsed command: {}", command);
          if (command instanceof IdSizesReplyCommand) {
            vmInformation.setIdSizes(((IdSizesReplyCommand) command).getIdSizes());
          }
        }
        synchronized (debuggers) {
          for (DebuggerConnection debugger : debuggers) {
            debugger.getPacketStream().write(vmPacket);
          }
        }
      }

      synchronized (debuggers) {
        for (DebuggerConnection debugger : debuggers) {
          Packet debuggerPacket = debugger.getPacketStream().read();
          if (debuggerPacket != null) {
            log.info("Debugger: {}", debuggerPacket);
            Command command = commandParser.parse(debuggerPacket);
            log.info("Parsed command: {}", command);
            vm.getPacketStream().write(debuggerPacket);
          }
        }
      }
    }
  }

  private void waitForFirstDebuggerConnection() throws InterruptedException {
    synchronized (debuggers) {
      while (debuggers.isEmpty()) {
        debuggers.wait();
      }
    }
  }


  @Override
  public void close() throws Exception {
    debuggerConnectionThread.interrupt();
    vm.close();
    synchronized (debuggers) {
      for (DebuggerConnection debugger : debuggers) {
        debugger.close();
      }
    }
  }
}
