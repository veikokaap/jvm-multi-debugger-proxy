package kaap.veiko.debuggerforker.managers;

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

class CommandStreamManager {
  private static final Logger log = LoggerFactory.getLogger(CommandStreamManager.class);

  private final Map<PacketSource, CommandStream> sourceStreamMap = new ConcurrentHashMap<>();
  private final Set<CommandStream> streamsMarkedForClosing = ConcurrentHashMap.newKeySet();

  private final ConcurrentLinkedDeque<Command> readQueue = new ConcurrentLinkedDeque<>();
  private final Map<PacketSource, Deque<Command>> writeQueues = new ConcurrentHashMap<>();

  void markForClosingAfterAllPacketsWritten(PacketSource source) {
    CommandStream stream = sourceStreamMap.remove(source);
    if (stream == null) {
      log.warn("Trying to remove a packet source already removed {}", source);
    }
    else {
      streamsMarkedForClosing.add(stream);
    }
  }

  boolean markedForClosing(PacketSource source) {
    return streamsMarkedForClosing.stream()
        .map(CommandStream::getSource)
        .anyMatch(source::equals);
  }

  void add(CommandStream commandStream) {
    sourceStreamMap.put(commandStream.getSource(), commandStream);
    writeQueues.put(commandStream.getSource(), new ConcurrentLinkedDeque<>());
  }

  ConcurrentLinkedDeque<Command> getReadQueue() {
    return readQueue;
  }

  Deque<Command> getWriteQueue(PacketSource packetSource) {
    return writeQueues.get(packetSource);
  }

  private Set<PacketSource> allSources() {
    return sourceStreamMap.keySet();
  }

  PacketSource getVmSource() {
    return allSources().stream()
        .filter(PacketSource::isVirtualMachine)
        .findFirst()
        .orElse(null);
  }

  List<PacketSource> getAllDebuggers() {
    return writeQueues.keySet().stream()
        .filter(PacketSource::isDebugger)
        .collect(Collectors.toList());
  }

  boolean checkSourceForRemoval(PacketSource source) {
    if (markedForClosing(source) && getWriteQueue(source).isEmpty()) {
      removeSource(source);
      return true;
    }

    return false;
  }

  private void removeSource(PacketSource source) {
    CommandStream stream = sourceStreamMap.remove(source);
    streamsMarkedForClosing.remove(stream);
    writeQueues.remove(source);
  }
}
