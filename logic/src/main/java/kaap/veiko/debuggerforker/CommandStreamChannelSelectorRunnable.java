package kaap.veiko.debuggerforker;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaap.veiko.debuggerforker.commands.Command;
import kaap.veiko.debuggerforker.commands.CommandStream;
import kaap.veiko.debuggerforker.packet.PacketSource;

public class CommandStreamChannelSelectorRunnable implements Runnable {

  private final Logger log = LoggerFactory.getLogger(CommandStreamChannelSelectorRunnable.class);

  private final Object selectLock = new Object();
  private final Object registerLock = new Object();

  private final AtomicBoolean open = new AtomicBoolean(true);

  private final Selector selector;
  private final Consumer<Command> readPacketConsumer;
  private final Function<PacketSource, Command> packetToWriteProducer;

  private final Set<PacketSource> sourcesMarkedForClosing = ConcurrentHashMap.newKeySet();

  public static CommandStreamChannelSelectorRunnable create(Consumer<Command> readCommandConsumer, Function<PacketSource, Command> writePacketProducer) throws IOException {
    return new CommandStreamChannelSelectorRunnable(Selector.open(), readCommandConsumer, writePacketProducer);
  }

  private CommandStreamChannelSelectorRunnable(Selector selector, Consumer<Command> readCommandConsumer, Function<PacketSource, Command> packetToWriteProducer) {
    this.selector = selector;
    this.readPacketConsumer = readCommandConsumer;
    this.packetToWriteProducer = packetToWriteProducer;
  }

  public void addCommandStream(CommandStream commandStream) {
    try {
      synchronizedRegister(commandStream);
    }
    catch (Exception e) {
      commandStream.close();
      log.error("Error while trying to register a channel", e);
    }
  }

  private void synchronizedRegister(CommandStream commandStream) throws ClosedChannelException {
    synchronized (registerLock) {
      selector.wakeup();
      synchronized (selectLock) { // Wait until select has woken.
        commandStream.getSocketChannel().register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE, commandStream);
        log.info("Registered new commandStream: {}", commandStream);
      }
    }
  }

  private int synchronizedSelect() throws IOException {
    int selected;
    synchronized (selectLock) {
      selected = selector.select();
    }
    synchronized (registerLock) {} // Wait until new channel is registered

    return selected;
  }

  @Override
  public void run() {
    try {
      selectLoop();
    }
    catch (Exception e) {
      log.error("Thread '{}' will shut down due to exception in runnable", Thread.currentThread(), e);
    }
  }

  private void selectLoop() {
    while (open.get() && !Thread.currentThread().isInterrupted()) {
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
      selectionKeys.clear();
    }
  }

  private void readWriteCommand(SelectionKey key, CommandStream commandStream) throws IOException {
    if (key.isReadable()) {
      readCommand(commandStream);
    }
    if (key.isWritable()) {
      writeCommand(commandStream);
    }
    closeIfMarked(commandStream);
  }

  private void writeCommand(CommandStream commandStream) throws IOException {
    Command command = packetToWriteProducer.apply(commandStream.getSource());
    if (command != null) {
      commandStream.write(command);
    }
  }

  private void closeIfMarked(CommandStream commandStream) {
    if (sourcesMarkedForClosing.contains(commandStream.getSource())) {
      commandStream.close();
    }
  }

  private void readCommand(CommandStream commandStream) throws IOException {
    Command command = commandStream.read();
    if (command != null) {
      readPacketConsumer.accept(command);
    }
  }
  
  public void close() {
    open.set(false);
  }

  /**
   * Close the CommandStream corresponding to the source the next time it's reads/writes are done. This is done to ensure the last packet is sent before closing.
   */
  public void markForClosing(PacketSource source) {
    sourcesMarkedForClosing.add(source);
  }
}
