package kaap.veiko.debuggerforker;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.schedulers.Schedulers;
import kaap.veiko.debuggerforker.commands.CommandPacket;
import kaap.veiko.debuggerforker.commands.CommandPacketStream;
import kaap.veiko.debuggerforker.connections.DebuggerManager;
import kaap.veiko.debuggerforker.connections.VirtualMachineManager;
import kaap.veiko.debuggerforker.connections.connectors.DebuggerConnector;
import kaap.veiko.debuggerforker.connections.connectors.VMConnector;
import kaap.veiko.debuggerforker.packet.Packet;
import kaap.veiko.debuggerforker.types.VMInformation;

public class DebuggerForker implements AutoCloseable {

  private final static Logger log = LoggerFactory.getLogger(DebuggerForker.class);

  private final VirtualMachineManager vm;
  private final VMInformation vmInformation = new VMInformation();
  private final ProxyPacketStream proxyPacketStream = new ProxyPacketStream();

  private DebuggerForker(VirtualMachineManager vm, int debuggerPort) throws IOException {
    this.vm = vm;

    DebuggerConnector debuggerConnector = new DebuggerConnector(debuggerPort);
    Single.create((SingleEmitter<DebuggerManager> subscriber) -> {
      try {
        subscriber.onSuccess(debuggerConnector.getConnectionBlocking());
      }
      catch (IOException e) {
        subscriber.onError(e);
      }
    }).repeat().toObservable().map(DebuggerManager::getPacketStream).map(ps -> new CommandPacketStream(ps, vmInformation)).subscribeOn(Schedulers.newThread()).subscribe(
        proxyPacketStream::addPacketStream,
        error -> log.error("Error during debugger connection", error)
    );
  }

  public static void start(InetSocketAddress virtualMachineAddress, int debuggerPort) throws IOException, InterruptedException {
    VirtualMachineManager vm = VMConnector.connectToVM(virtualMachineAddress);

    DebuggerForker debuggerForker = new DebuggerForker(vm, debuggerPort);
    debuggerForker.start();
  }

  private void start() throws IOException, InterruptedException {
    CommandPacketStream vmPacketStream = new CommandPacketStream(vm.getPacketStream(), vmInformation);

    Observable.<CommandPacket>create(subscriber -> {
      while (!subscriber.isDisposed()) {
        CommandPacket packet = proxyPacketStream.read();
        if (packet != null) {
          log.info("Packet from debugger {}", packet);
          subscriber.onNext(packet);
        }
      }
      subscriber.onComplete();
    }).subscribeOn(Schedulers.newThread()).subscribe(
        packet -> {
          vmPacketStream.write(packet);
        },
        error -> {},
        () -> log.info("ProxyPacketStream has finished work")
    );

    while (!Thread.interrupted()) {
      CommandPacket packet = vmPacketStream.read();
      if (packet != null) {
        log.info("Packet from VM {}", packet);
        proxyPacketStream.write(packet);
      }
    }
  }


  @Override
  public void close() throws Exception {
  }
}
