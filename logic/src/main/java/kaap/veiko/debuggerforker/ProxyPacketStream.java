package kaap.veiko.debuggerforker;

import java.io.IOException;

import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import kaap.veiko.debuggerforker.commands.Command;
import kaap.veiko.debuggerforker.commands.CommandStream;

public class ProxyPacketStream implements AutoCloseable {

  private final Logger log = LoggerFactory.getLogger(ProxyPacketStream.class);

  private final Map<CommandStream, List<Disposable>> packetStreamDisposables = new HashMap<>();
  private final Deque<Command> packets = new ConcurrentLinkedDeque<>();
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
    Command command = packets.pollFirst();
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
    Disposable readDisposable = Observable.<Command>create(subscriber -> {
      try {
        while (!subscriber.isDisposed()) {
          Command command = stream.read();
          if (command != null) {
            subscriber.onNext(command);
          }
        }
        subscriber.onComplete();
      }
      catch (IOException exception) {
        subscriber.onError(exception);
      }
    }).subscribeOn(Schedulers.newThread()).subscribe(
        packets::addLast,
        error -> {
          log.error("Exception thrown when reading packets from a getPacket stream {}.", stream, error);
          try {
            stream.close();
          }
          catch (Exception e) {
            log.error("Error when closing stream {}", stream, e);
          }
        },
        () -> {
          log.info("Finished reading from {}", stream);
          try {
            stream.close();
          }
          catch (Exception e) {
            log.error("Error when closing stream {}", stream, e);
          }
        }
    );

    Disposable writeDisposable = writtenPackets.subscribeOn(Schedulers.newThread()).subscribe(
        stream::write
    );

    packetStreamDisposables.put(stream, Arrays.asList(readDisposable, writeDisposable));
  }
}
