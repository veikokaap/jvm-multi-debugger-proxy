package kaap.veiko.debuggerforker;

import java.io.IOException;
import java.nio.channels.Selector;
import java.util.Deque;
import java.util.List;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaap.veiko.debuggerforker.commands.Command;
import kaap.veiko.debuggerforker.commands.CommandStream;
import kaap.veiko.debuggerforker.managers.CommandStreamInputOutputManager;
import kaap.veiko.debuggerforker.packet.PacketSource;

public class ProxyCommandStream {

  private final Logger log = LoggerFactory.getLogger(ProxyCommandStream.class);

  private final CommandStreamInputOutputManager commandStreamManager;
  private final Thread managerThread;

  public static ProxyCommandStream create() throws IOException {
    return new ProxyCommandStream(Selector.open());
  }

  private ProxyCommandStream(Selector selector) {
    commandStreamManager = new CommandStreamInputOutputManager(selector);
    this.managerThread = new Thread(commandStreamManager);
  }

  void registerCommandStream(CommandStream commandStream) throws Exception {
    commandStreamManager.addCommandStream(commandStream);
  }

  public void markForClosingAfterAllPacketsWritten(PacketSource source) {
    commandStreamManager.markForClosing(source);
  }

  public @Nullable Command read() {
    Command readCommand = commandStreamManager.getReadQueue().pollFirst();
    if (readCommand != null) {
      log.info("Command from readQueue: {}", readCommand);
    }
    return readCommand;
  }

  public void writeToVm(Command command) {
    PacketSource vmSource = commandStreamManager.getVmSource();
    if (vmSource != null) {
      write(vmSource, command);
    }
  }

  public void writeToAllDebuggers(Command command) {
    commandStreamManager.getAllDebuggers().forEach(s -> write(s, command));
  }

  public void write(PacketSource source, Command command) {
    Deque<Command> writeQueue = commandStreamManager.getWriteQueue(source);
    if (writeQueue != null) {
      log.info("Adding command '{}' to writeQueue for source {}", command, source);
      writeQueue.addLast(command);
    } else {
      log.warn("Trying to write to a packet source which isn't registered: {}", source);
    }
  }

  public PacketSource getVmSource() {
    return commandStreamManager.getVmSource();
  }

  public List<PacketSource> getAllDebuggers() {
    return commandStreamManager.getAllDebuggers();
  }

  void start() {
    managerThread.start();
  }

  boolean isOpen() {
    return commandStreamManager.isOpen();
  }

  void close() {
    commandStreamManager.close();
  }
}
