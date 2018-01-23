package kaap.veiko.debuggerforker.connections;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import kaap.veiko.debuggerforker.packet.PacketStream;

public abstract class ConnectorBase<T extends PacketStream> implements AutoCloseable {
  
  private final Thread thread;
  private final AtomicBoolean open = new AtomicBoolean(true);
  private final Set<T> connectionHistory = new ConcurrentSkipListSet<>();
  private final int maxOpenConnections;

  protected ConnectorBase(Consumer<T> listener, int maxOpenConnections, String threadName) {
    this.maxOpenConnections = maxOpenConnections;
    thread = new Thread(() -> {
      while (isOpen()) {
        connect(listener);
      }
    }, threadName);
  }

  private void connect(Consumer<T> listener) {
    if (shouldConnect()) {
      try {
        T connection = getConnectionBlocking();
        if (connection != null) {
          listener.accept(connection);
          connectionHistory.add(connection);
        }
      }
      catch (Exception e) {
        open.set(false); 
      }
    }
    else {
      try {
        Thread.sleep(50);
      }
      catch (InterruptedException e) {
        open.set(false);
        Thread.currentThread().interrupt();
      }
    }
  }

  private boolean shouldConnect() {
    removeClosedConnectionsFromHistory();
    return maxOpenConnections > connectionHistory.size();
  }

  private void removeClosedConnectionsFromHistory() {
    connectionHistory.stream()
        .filter(PacketStream::isClosed)
        .forEach(connectionHistory::remove);
  }

  private boolean isOpen() {
    return open.get() && !Thread.currentThread().isInterrupted();
  }

  protected abstract T getConnectionBlocking() throws IOException;
  
  public void start() {
    thread.start();
  }

  @Override
  public void close() {
    open.set(false);

    try {
      thread.join(100);
    }
    catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
    finally {
      thread.interrupt();
    }
  }
}
