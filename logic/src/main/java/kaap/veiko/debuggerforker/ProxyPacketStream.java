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
import kaap.veiko.debuggerforker.commands.CommandPacket;
import kaap.veiko.debuggerforker.commands.CommandPacketStream;
import kaap.veiko.debuggerforker.commands.sets.virtualmachine.DisposeCommand;
import kaap.veiko.debuggerforker.packet.PacketImpl;

public class ProxyPacketStream implements AutoCloseable {

  private final Logger log = LoggerFactory.getLogger(ProxyPacketStream.class);

  private final Map<CommandPacketStream, List<Disposable>> packetStreamDisposables = new HashMap<>();
  private final Deque<CommandPacket> packets = new ConcurrentLinkedDeque<>();
  private final PublishSubject<CommandPacket> writtenPackets = PublishSubject.create();

  public void addPacketStream(CommandPacketStream stream) {
    observePackets(stream);
  }

  public void removePacketStream(CommandPacketStream stream) {
    packetStreamDisposables.remove(stream).forEach(Disposable::dispose);
  }

  public CommandPacket read() {
    CommandPacket packet = packets.pollFirst();
    if (packet == null) {
      return null;
    }

    return packet;
  }

  public void write(CommandPacket packet) {
    writtenPackets.onNext(packet);
  }

  public void close() {
    packetStreamDisposables.values().stream()
        .flatMap(Collection::stream)
        .forEach(Disposable::dispose);
  }

  private void observePackets(CommandPacketStream stream) {
    Disposable readDisposable = Observable.<CommandPacket>create(subscriber -> {
      try {
        while (!subscriber.isDisposed()) {
          CommandPacket packet = stream.read();
          if (packet != null) {
            subscriber.onNext(packet);
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
          log.error("Exception thrown when reading packets from a packet stream {}.", stream, error);
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
