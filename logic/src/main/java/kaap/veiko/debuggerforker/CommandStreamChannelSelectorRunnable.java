package kaap.veiko.debuggerforker;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaap.veiko.debuggerforker.commands.Command;
import kaap.veiko.debuggerforker.commands.CommandStream;

public class CommandStreamChannelSelectorRunnable implements Runnable {

  private final Logger log = LoggerFactory.getLogger(CommandStreamChannelSelectorRunnable.class);

  private final Object selectLock = new Object();
  private final Object registerLock = new Object();

  private final Selector selector;
  private final Consumer<Command> readPacketConsumer;
  private final Function<CommandStream, Command> packetToWriteProducer;

  public static CommandStreamChannelSelectorRunnable create(Consumer<Command> readCommandConsumer, Function<CommandStream, Command> writePacketProducer) throws IOException {
    return new CommandStreamChannelSelectorRunnable(Selector.open(), readCommandConsumer, writePacketProducer);
  }

  private CommandStreamChannelSelectorRunnable(Selector selector, Consumer<Command> readCommandConsumer, Function<CommandStream, Command> packetToWriteProducer) {
    this.selector = selector;
    this.readPacketConsumer = readCommandConsumer;
    this.packetToWriteProducer = packetToWriteProducer;
  }

  public void addCommandStream(CommandStream commandStream) {
    try {
      synchronized (selectLock) {
        selector.wakeup();
        synchronized (registerLock) {
          commandStream.getSocketChannel().register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE, commandStream);
          log.info("Registered new debugger: {}", commandStream);
        }
      }
    }
    catch (Exception e) {
      commandStream.close();
      log.error("Error while trying to register a channel", e);
    }
  }

  private int synchronizedSelect() throws IOException {
    int selected;
    synchronized (registerLock) {
      selected = selector.select();
    }
    synchronized (selectLock) {} // Wait until new channel is registered

    return selected;
  }

  @Override
  public void run() {
    while (!Thread.interrupted()) {
      try {
        int selected = synchronizedSelect();
        if (selected == 0) continue;
      }
      catch (IOException e) {
        log.error("Aborting reading/writing commands due to exception.", e);
        return;
      }
      Set<SelectionKey> selectionKeys = selector.selectedKeys();
      for (SelectionKey key : selectionKeys) {
        if (key.attachment() instanceof CommandStream) {
          CommandStream commandStream = (CommandStream) key.attachment();
          try {
            readWriteCommand(key, commandStream);
          } catch (Exception exception) {
            log.error("Error while reading/writing with channel", exception);
            commandStream.close();
            key.cancel();
          }
        }
      }
    }
  }

  private void readWriteCommand(SelectionKey key, CommandStream commandStream) throws IOException {
    if (key.isReadable()) {
      readCommand(commandStream);
    }
    if (key.isWritable()) {
      writeCommand(commandStream);
    }
  }

  private void writeCommand(CommandStream commandStream) throws IOException {
    Command command = packetToWriteProducer.apply(commandStream);
    if (command != null) {
      commandStream.write(command);
    }
  }

  private void readCommand(CommandStream commandStream) throws IOException {
    Command command = commandStream.read();
    if (command != null) {
      readPacketConsumer.accept(command);
    }
  }
}
