package kaap.veiko.debuggerforker;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.schedulers.Schedulers;
import kaap.veiko.debuggerforker.commands.CommandPacket;
import kaap.veiko.debuggerforker.commands.CommandPacketStream;
import kaap.veiko.debuggerforker.connections.DebuggerManager;
import kaap.veiko.debuggerforker.connections.VirtualMachineManager;
import kaap.veiko.debuggerforker.connections.connectors.DebuggerConnector;
import kaap.veiko.debuggerforker.connections.connectors.VMConnector;
import kaap.veiko.debuggerforker.types.VMInformation;

public class DebuggerForker implements AutoCloseable {

  private final static Logger log = LoggerFactory.getLogger(DebuggerForker.class);

  private final VirtualMachineManager vm;
  private final VMInformation vmInformation = new VMInformation();
  private final List<DebuggerManager> debuggers = new CopyOnWriteArrayList<>();
  private final Observable<DebuggerManager> debuggerConnections;

  private DebuggerForker(VirtualMachineManager vm, int debuggerPort) throws IOException {
    this.vm = vm;

    DebuggerConnector debuggerConnector = new DebuggerConnector(debuggerPort);
    debuggerConnections = Single.create((SingleEmitter<DebuggerManager> subscriber) -> {
      try {
        subscriber.onSuccess(debuggerConnector.getConnectionBlocking());
      }
      catch (IOException e) {
        subscriber.onError(e);
      }
    }).repeat().toObservable();
    debuggerConnections.doOnDispose(debuggerConnector::close);
  }

  public static void start(InetSocketAddress virtualMachineAddress, int debuggerPort) throws IOException, InterruptedException {
    VirtualMachineManager vm = VMConnector.connectToVM(virtualMachineAddress);

    DebuggerForker debuggerForker = new DebuggerForker(vm, debuggerPort);
    debuggerForker.start();
  }

  private void start() throws IOException, InterruptedException {
    debuggerConnections
        .subscribeOn(Schedulers.newThread())
        .subscribe(debuggers::add, error -> log.error("Error when debugger connecting", error));

    Observable<CommandPacketStream> debuggerPacketStreams =
        Observable.create((ObservableEmitter<DebuggerManager> subscriber) -> {
          debuggers.forEach(subscriber::onNext);
          subscriber.onComplete();
        })
            .repeat()
            .map(DebuggerManager::getPacketStream)
            .map(ps -> new CommandPacketStream(ps, vmInformation));

    Observable<CommandPacket> vmPackets =
        Single.just(new CommandPacketStream(vm.getPacketStream(), vmInformation))
            .repeat()
            .map(ps -> Optional.ofNullable(ps.read()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .toObservable();

    vmPackets
        .subscribeOn(Schedulers.newThread())
        .subscribe(packet -> {
              log.info("Packet from VM: {}", packet);
              for (DebuggerManager debugger : debuggers) {
                debugger.getPacketStream().write(packet);
              }
            },
            error -> log.error("Error when reading vm packets {}", error.getMessage())
        );

    Observable<CommandPacket> debuggerPackets = debuggerPacketStreams
        .map(ps -> Optional.ofNullable(ps.read()))
        .filter(Optional::isPresent)
        .map(Optional::get);

    debuggerPackets
        .subscribe(
            packet -> {
              log.info("Packet from debugger: {}", packet);
              vm.getPacketStream().write(packet);
            },
            error -> log.error("Error when reading debugger packets {}", error.getMessage())
        );

  }

  @Override
  public void close() throws Exception {
  }
}
