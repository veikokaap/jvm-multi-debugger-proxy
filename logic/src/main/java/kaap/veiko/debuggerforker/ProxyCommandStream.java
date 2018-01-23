package kaap.veiko.debuggerforker;

import java.io.IOException;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaap.veiko.debuggerforker.commands.Command;
import kaap.veiko.debuggerforker.commands.CommandStream;

public class ProxyCommandStream {

  private final Logger log = LoggerFactory.getLogger(ProxyCommandStream.class);

  private final ConcurrentLinkedDeque<Command> readQueue = new ConcurrentLinkedDeque<>();
  private final Map<CommandStream, Deque<Command>> writeQueues = new ConcurrentHashMap<>();
  private final CommandStreamChannelSelectorRunnable channelSelectorRunnable;
  private final Thread thread;
  
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
    writeQueues.put(commandStream, new ConcurrentLinkedDeque<>());
    channelSelectorRunnable.addCommandStream(commandStream);
  }

  public Command read() {
    Command readCommand = readQueue.pollFirst();
    log.info("Command from readQueue: {}", readCommand);
    return readCommand;
  }

  public void writeToAll(Command command) {
    for (CommandStream commandStream : writeQueues.keySet()) {
      write(commandStream, command);
    }
  }

  public void write(CommandStream commandStream, Command command) {
    Deque<Command> writeQueue = writeQueues.get(commandStream);
    if (writeQueue != null) {
      log.info("Adding to writeQueue: {}", command);
      writeQueue.addLast(command);
    } else {
      log.warn("Trying to write to a CommandStream which isn't registered: {}", commandStream);
    }
  }

  private void readFromRunnable(Command command) {
    readQueue.addLast(command);
  }

  private Command writeToRunnable(CommandStream commandStream) {
    Deque<Command> writeQueue = writeQueues.get(commandStream);
    if (writeQueue != null) {
      return writeQueue.pollFirst();
    } else {
      log.warn("Trying to find a writable command from a CommandStream which isn't registered: {}", commandStream);
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
