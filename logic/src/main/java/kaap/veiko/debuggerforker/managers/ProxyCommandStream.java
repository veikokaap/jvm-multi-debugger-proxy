package kaap.veiko.debuggerforker.managers;

import java.io.IOException;
import java.nio.channels.Selector;
import java.util.Deque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaap.veiko.debuggerforker.commands.Command;
import kaap.veiko.debuggerforker.commands.CommandStream;
import kaap.veiko.debuggerforker.packet.PacketSource;

public class ProxyCommandStream extends CommandStreamIoRunnable {

  private final Logger log = LoggerFactory.getLogger(ProxyCommandStream.class);

  private final CommandStreamManager commandStreamManager = new CommandStreamManager();

  public static ProxyCommandStream create() throws IOException {
    return new ProxyCommandStream(Selector.open());
  }

  private ProxyCommandStream(Selector selector) throws IOException {
    super(selector);
  }

  public void markForClosingAfterAllPacketsWritten(PacketSource source) {
    commandStreamManager.markForClosingAfterAllPacketsWritten(source);
  }

  public void registerCommandStream(CommandStream commandStream) throws Exception {
    commandStreamManager.add(commandStream);
    register(commandStream);
  }

  public Command read() {
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
    /* Don't add any new packets to be written to a stream marked for closing */
    if (commandStreamManager.markedForClosing(source)) {
      return;
    }

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

  @Override
  void consumeReadCommand(Command command) {
    commandStreamManager.getReadQueue().addLast(command);
  }

  @Override
  Command getWriteCommand(PacketSource source) {
    if (source.isHoldEvents()) {
      return null;
    }

    Deque<Command> writeQueue = commandStreamManager.getWriteQueue(source);
    if (writeQueue != null) {
      Command command = writeQueue.pollFirst();
      if (commandStreamManager.checkSourceForRemoval(source)) {
        markForClosing(source);
      }
      return command;
    } else {
      log.warn("Trying to find a writable command from a packet source which isn't registered: {}", source);
      return null;
    }
  }

}
