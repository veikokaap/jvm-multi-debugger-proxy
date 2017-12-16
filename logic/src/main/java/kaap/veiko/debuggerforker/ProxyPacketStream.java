package kaap.veiko.debuggerforker;

import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import kaap.veiko.debuggerforker.commands.Command;
import kaap.veiko.debuggerforker.commands.CommandStream;
import kaap.veiko.debuggerforker.utils.ObservableUtil;

public class ProxyPacketStream implements AutoCloseable {

  private final Logger log = LoggerFactory.getLogger(ProxyPacketStream.class);

  private final Map<CommandStream, List<Disposable>> packetStreamDisposables = new HashMap<>();
  private final Deque<Command> commands = new ConcurrentLinkedDeque<>();
  private final PublishSubject<Command> writtenPackets = PublishSubject.create();

  public int size() {
    return packetStreamDisposables.size();
  }

  public void addPacketStream(CommandStream stream) {
    observePackets(stream);
  }

  public void removePacketStream(CommandStream stream) {
    packetStreamDisposables.remove(stream).forEach(Disposable::dispose);
  }

  public Command read() {
    Command command = commands.pollFirst();
    if (command == null) {
      return null;
    }

    return command;
  }

  public void write(Command command) {
    writtenPackets.onNext(command);
  }

  public void close() {
    packetStreamDisposables.values().stream()
        .flatMap(Collection::stream)
        .forEach(Disposable::dispose);
  }

  private void observePackets(CommandStream stream) {
    Disposable readDisposable = ObservableUtil.commandStreamObservable(stream)
        .subscribeOn(Schedulers.io())
        .subscribe(
            this::handleReadCommand,
            error -> handleReadError(stream, error),
            () -> handleReadComplete(stream)
        );

    Disposable writeDisposable = writtenPackets.subscribeOn(Schedulers.io()).subscribe(
        stream::write
    );

    packetStreamDisposables.put(stream, Arrays.asList(readDisposable, writeDisposable));
  }

  private void handleReadCommand(Command command) {
    commands.add(command);
  }

  private void handleReadComplete(CommandStream stream) {
    log.info("Finished reading from {}", stream);
    closeStream(stream);
  }

  private void handleReadError(CommandStream stream, Throwable error) {
    log.error("Exception thrown when reading commands from a getPacket stream {}.", stream, error);
    closeStream(stream);
  }

  private void closeStream(CommandStream stream) {
    try {
      if (!stream.isClosed()) {
        stream.close();
      }
    }
    catch (Exception e) {
      log.error("Error when closing stream {}", stream, e);
    }
  }

}
