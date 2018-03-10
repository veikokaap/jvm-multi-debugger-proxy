package kaap.veiko.debuggerforker.managers;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaap.veiko.debuggerforker.commands.Command;
import kaap.veiko.debuggerforker.commands.CommandStream;
import kaap.veiko.debuggerforker.packet.PacketSource;

public class CommandStreamInputOutputManager extends ChannelInputOutputManager<CommandStream> {

  private final Logger log = LoggerFactory.getLogger(CommandStreamInputOutputManager.class);

  private final Set<PacketSource> sourcesMarkedForClosing = ConcurrentHashMap.newKeySet();

  private final Map<PacketSource, CommandStream> sourceStreamMap = new ConcurrentHashMap<>();
  private final ConcurrentLinkedDeque<Command> readQueue = new ConcurrentLinkedDeque<>();
  private final Map<PacketSource, Deque<Command>> writeQueues = new ConcurrentHashMap<>();

  public CommandStreamInputOutputManager(Selector selector) {
    super(selector);
  }

  public void addCommandStream(CommandStream commandStream) throws Exception {
    register(commandStream);
  }

  public ConcurrentLinkedDeque<Command> getReadQueue() {
    return readQueue;
  }

  public Deque<Command> getWriteQueue(PacketSource packetSource) {
    return writeQueues.get(packetSource);
  }

  private Set<PacketSource> allSources() {
    return sourceStreamMap.keySet();
  }

  public PacketSource getVmSource() {
    return allSources().stream()
        .filter(PacketSource::isVirtualMachine)
        .findFirst()
        .orElse(null);
  }

  public List<PacketSource> getAllDebuggers() {
    return writeQueues.keySet().stream()
        .filter(PacketSource::isDebugger)
        .collect(Collectors.toList());
  }

  private void addToReadQueue(Command command) {
    readQueue.addLast(command);
  }

  private Command getFromWriteQueue(PacketSource source) {
    if (source.isHoldEvents()) {
      return null;
    }

    Deque<Command> writeQueue = writeQueues.get(source);
    if (writeQueue != null) {
      return writeQueue.pollFirst();
    } else {
      log.warn("Trying to find a writable command from a packet source which isn't registered: {}", source);
      return null;
    }
  }

  @Override
  void internalRegister(CommandStream stream, Selector selector) throws ClosedChannelException {
    stream.getSocketChannel().register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE, stream);
    sourceStreamMap.put(stream.getSource(), stream);
    writeQueues.put(stream.getSource(), new ConcurrentLinkedDeque<>());
  }

  @Override
  void handleKey(SelectionKey key) {
    if (key.attachment() instanceof CommandStream) {
      CommandStream commandStream = (CommandStream) key.attachment();
      try {
        readWriteCommand(key, commandStream);
      } catch (Exception exception) {
        log.error("Error while reading/writing with channel. Removing {}", commandStream, exception);
        commandStream.close();
        key.cancel();
      }
    }
  }

  private void readWriteCommand(SelectionKey key, CommandStream commandStream) throws IOException {
    if (key.isReadable() && !isMarkedForClosing(commandStream.getSource())) {
      Command command = commandStream.read();
      if (command != null) {
        addToReadQueue(command);
      }
    }

    if (key.isWritable()) {
      Command command = getFromWriteQueue(commandStream.getSource());
      if (command != null) {
        commandStream.write(command);
      }
    }

    closeIfMarkedAndWriteQueueEmpty(commandStream);
  }

  /**
   * Close the CommandStream corresponding to the source once all necessary packets are written to it. This is done to ensure the last packets are sent before closing.
   */
  public void markForClosing(PacketSource source) {
    sourcesMarkedForClosing.add(source);
  }

  private boolean isMarkedForClosing(PacketSource source) {
    return sourcesMarkedForClosing.contains(source);
  }

  private void closeIfMarkedAndWriteQueueEmpty(CommandStream commandStream) {
    PacketSource source = commandStream.getSource();
    if (sourcesMarkedForClosing.contains(source) && writeQueues.get(source).isEmpty()) {
      commandStream.close();
      sourceStreamMap.remove(source);
      sourcesMarkedForClosing.remove(source);
      writeQueues.remove(source);
    }
  }

}
