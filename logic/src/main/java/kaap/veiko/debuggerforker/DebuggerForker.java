package kaap.veiko.debuggerforker;

import java.io.IOException;
import java.net.InetSocketAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.schedulers.Schedulers;
import kaap.veiko.debuggerforker.commands.Command;
import kaap.veiko.debuggerforker.commands.CommandStream;
import kaap.veiko.debuggerforker.connections.DebuggerManager;
import kaap.veiko.debuggerforker.connections.VirtualMachineManager;
import kaap.veiko.debuggerforker.connections.connectors.DebuggerConnector;
import kaap.veiko.debuggerforker.connections.connectors.VMConnector;
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
    }).repeat().toObservable().map(DebuggerManager::getPacketStream).map(ps -> new CommandStream(ps, vmInformation)).subscribeOn(Schedulers.newThread()).subscribe(
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
    CommandStream vmCommandStream = new CommandStream(vm.getPacketStream(), vmInformation);

    Observable.<Command>create(subscriber -> {
      while (!subscriber.isDisposed()) {
        Command command = proxyPacketStream.read();
        if (command != null) {
          log.info("Packet from debugger {}", command);
          subscriber.onNext(command);
        }
      }
      subscriber.onComplete();
    }).subscribeOn(Schedulers.newThread()).subscribe(
        command -> {
          vmCommandStream.write(command);
        },
        error -> {},
        () -> log.info("ProxyPacketStream has finished work")
    );

    while (!Thread.interrupted()) {
      Command command = vmCommandStream.read();
      if (command != null) {
        log.info("Packet from VM {}", command);
        proxyPacketStream.write(command);
      }
    }
  }


  @Override
  public void close() throws Exception {
  }
}
