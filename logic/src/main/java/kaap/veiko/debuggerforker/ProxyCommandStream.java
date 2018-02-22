package kaap.veiko.debuggerforker;

import java.io.IOException;
import java.util.Deque;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentSkipListSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaap.veiko.debuggerforker.commands.Command;
import kaap.veiko.debuggerforker.commands.CommandStream;
import kaap.veiko.debuggerforker.packet.PacketSource;

public class ProxyCommandStream {

  private final Logger log = LoggerFactory.getLogger(ProxyCommandStream.class);

  private final ConcurrentLinkedDeque<Command> readQueue = new ConcurrentLinkedDeque<>();
  private final Map<PacketSource, Deque<Command>> writeQueues = new ConcurrentHashMap<>();
  private final CommandStreamChannelSelectorRunnable channelSelectorRunnable;
  private final Thread thread;

  private final Map<PacketSource, CommandStream> sourceStreamMap = new ConcurrentHashMap<>();

  private final Set<CommandStream> streamsMarkedForClosing = new ConcurrentSkipListSet<>();

  public static ProxyCommandStream create() throws IOException {
    return new ProxyCommandStream();
  }

  private ProxyCommandStream() throws IOException {
    this.channelSelectorRunnable = CommandStreamChannelSelectorRunnable.create(this::readFromRunnable, this::writeToRunnable);
    thread = new Thread(channelSelectorRunnable, "ProxyCommandStreamThread");
  }

  public void start() {
    thread.start();
  }

  public void addCommandStream(CommandStream commandStream) {
    PacketSource source = commandStream.getSource();

    sourceStreamMap.put(source, commandStream);
    writeQueues.put(source, new ConcurrentLinkedDeque<>());
    channelSelectorRunnable.addCommandStream(commandStream);
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
    log.info("Command from readQueue: {}", readCommand);
    return readCommand;
  }

  public void writeToAll(Command command) {
    for (PacketSource source : writeQueues.keySet()) {
      write(source, command);
    }
  }

  public void write(PacketSource source, Command command) {
    /* Don't add any new packets to be written to a stream marked for closing */
    if (markedForClosing(source)) {
      return;
    }

    Deque<Command> writeQueue = writeQueues.get(source);
    if (writeQueue != null) {
      log.info("Adding to writeQueue: {}", command);
      writeQueue.addLast(command);
    } else {
      log.warn("Trying to write to a packet source which isn't registered: {}", source);
    }
  }

  private boolean markedForClosing(PacketSource source) {
    return streamsMarkedForClosing.stream().map(CommandStream::getSource).anyMatch(source::equals);
  }

  private void readFromRunnable(Command command) {
    readQueue.addLast(command);
  }

  private Command writeToRunnable(PacketSource source) {
    Deque<Command> writeQueue = writeQueues.get(source);
    if (writeQueue != null) {
      Command command = writeQueue.pollFirst();
      if (writeQueue.isEmpty()) {
        channelSelectorRunnable.markForClosing(source);
      }
      return command;
    } else {
      log.warn("Trying to find a writable command from a packet source which isn't registered: {}", source);
      return null;
    }
  }

  public void close() {
    channelSelectorRunnable.close();

    try {
      Thread.sleep(100);
    }
    catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
    finally {
      thread.interrupt();
    }
  }
}
