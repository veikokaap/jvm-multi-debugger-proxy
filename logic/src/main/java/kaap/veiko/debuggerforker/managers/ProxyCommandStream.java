package kaap.veiko.debuggerforker.managers;

import java.io.IOException;
import java.nio.channels.Selector;
import java.util.Deque;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaap.veiko.debuggerforker.commands.Command;
import kaap.veiko.debuggerforker.commands.CommandStream;
import kaap.veiko.debuggerforker.packet.PacketSource;

public class ProxyCommandStream extends CommandStreamManager  {

  private final Logger log = LoggerFactory.getLogger(ProxyCommandStream.class);

  private final ConcurrentLinkedDeque<Command> readQueue = new ConcurrentLinkedDeque<>();
  private final Map<PacketSource, Deque<Command>> writeQueues = new ConcurrentHashMap<>();

  private final Map<PacketSource, CommandStream> sourceStreamMap = new ConcurrentHashMap<>();

  private final Set<CommandStream> streamsMarkedForClosing = ConcurrentHashMap.newKeySet();

  private PacketSource vmSource;

  public static ProxyCommandStream create() throws IOException {
    return new ProxyCommandStream(Selector.open());
  }

  private ProxyCommandStream(Selector selector) throws IOException {
    super(selector);
  }

  public void registerCommandStream(CommandStream commandStream) throws Exception {
    PacketSource source = commandStream.getSource();

    sourceStreamMap.put(source, commandStream);
    writeQueues.put(source, new ConcurrentLinkedDeque<>());
    register(commandStream);

    if (source.isVirtualMachine()) {
      vmSource = source;
    }
  }

  public void markForClosingAfterAllPacketsWritten(PacketSource source) {
    CommandStream stream = sourceStreamMap.remove(source);
    if (stream == null) {
      log.warn("Trying to remove a packet source already removed {}", source);
    } else {
      streamsMarkedForClosing.add(stream);
    }
  }

  public Command read() {
    Command readCommand = readQueue.pollFirst();
    if (readCommand != null) {
      log.info("Command from readQueue: {}", readCommand);
    }
    return readCommand;
  }

  public void writeToAll(Command command) {
    for (PacketSource source : writeQueues.keySet()) {
      write(source, command);
    }
  }

  public void writeToVm(Command command) {
    if (vmSource != null) {
      write(vmSource, command);
    }
  }

  public void writeToAllDebuggers(Command command) {
    writeQueues.keySet().stream()
        .filter(PacketSource::isDebugger)
        .forEach(s -> write(s, command));
  }

  public void write(PacketSource source, Command command) {
    /* Don't add any new packets to be written to a stream marked for closing */
    if (markedForClosing(source)) {
      return;
    }

    Deque<Command> writeQueue = writeQueues.get(source);
    if (writeQueue != null) {
      log.info("Adding command '{}' to writeQueue for source {}", command, source);
      writeQueue.addLast(command);
    } else {
      log.warn("Trying to write to a packet source which isn't registered: {}", source);
    }
  }

  public PacketSource getVmSource() {
    return vmSource;
  }

  private boolean markedForClosing(PacketSource source) {
    return streamsMarkedForClosing.stream()
        .map(CommandStream::getSource)
        .anyMatch(source::equals);
  }

  @Override
  void consumeInCommand(Command command) {
    readQueue.addLast(command);
  }

  @Override
  Command produceSourceOutCommand(PacketSource source) {
    if (source.isHoldEvents()) {
      return null;
    }

    Deque<Command> writeQueue = writeQueues.get(source);
    if (writeQueue != null) {
      Command command = writeQueue.pollFirst();
      if (markedForClosing(source) && writeQueue.isEmpty()) {
        removeSource(source);
      }
      return command;
    } else {
      log.warn("Trying to find a writable command from a packet source which isn't registered: {}", source);
      return null;
    }
  }

  private void removeSource(PacketSource source) {
    CommandStream stream = sourceStreamMap.remove(source);
    streamsMarkedForClosing.remove(stream);

    markForClosing(source);
    writeQueues.remove(source);
  }

}
