package kaap.veiko.debuggerforker.managers;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaap.veiko.debuggerforker.commands.Command;
import kaap.veiko.debuggerforker.commands.CommandStream;
import kaap.veiko.debuggerforker.packet.PacketSource;

abstract class CommandStreamManager extends ChannelManager<CommandStream> {

  private final Logger log = LoggerFactory.getLogger(CommandStreamManager.class);

  private final Set<PacketSource> sourcesMarkedForClosing = ConcurrentHashMap.newKeySet();

  CommandStreamManager(Selector selector) {
    super(selector);
  }

  abstract Command getWriteCommand(PacketSource source);

  abstract void consumeReadCommand(Command command);

  @Override
  SelectableChannel toChannel(CommandStream commandStream) {
    return commandStream.getSocketChannel();
  }

  @Override
  void handleKey(SelectionKey key) {
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

  private void readWriteCommand(SelectionKey key, CommandStream commandStream) throws IOException {
    if (key.isReadable()) {
      Command command = commandStream.read();
      if (command != null) {
        consumeReadCommand(command);
      }
    }

    if (key.isWritable()) {
      Command command = getWriteCommand(commandStream.getSource());
      if (command != null) {
        commandStream.write(command);
      }
    }

    closeIfMarked(commandStream);
  }

  private void closeIfMarked(CommandStream commandStream) {
    if (sourcesMarkedForClosing.contains(commandStream.getSource())) {
      commandStream.close();
    }
  }

  /**
   * Close the CommandStream corresponding to the source the next time it's reads/writes are done. This is done to ensure the last packet is sent before closing.
   */
  protected void markForClosing(PacketSource source) {
    sourcesMarkedForClosing.add(source);
  }
}
