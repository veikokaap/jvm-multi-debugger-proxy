package kaap.veiko.debuggerforker;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class ChannelManager<T extends AutoCloseable> implements Runnable, AutoCloseable {
  private final Logger log = LoggerFactory.getLogger(ChannelManager.class);

  private final Object selectLock = new Object();
  private final Object registerLock = new Object();
  private final Selector selector;

  private final AtomicBoolean open = new AtomicBoolean(true);

  ChannelManager(Selector selector) {
    this.selector = selector;
  }

  abstract SelectableChannel toChannel(T object);

  abstract void handleKey(SelectionKey key);

  public void register(T object) throws Exception {
    if (!open.get()) {
      throw new IOException("Can't register object to closed runnable.");
    }

    try {
      synchronizedRegister(object);
    }
    catch (Exception registerException) {
      try {
        object.close();
      }
      catch (Exception closeException) {
        registerException.addSuppressed(closeException);
        throw registerException;
      }
    }
  }

  private void synchronizedRegister(T object) throws ClosedChannelException {
    synchronized (registerLock) {
      selector.wakeup();
      synchronized (selectLock) { // Wait until select has woken.
        toChannel(object).register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE, object);
        log.info("Registered new channel: {}", object);
      }
    }
  }

  @Override
  public void run() {
    try {
      while (isOpen() && !Thread.currentThread().isInterrupted()) {
        int selected = synchronizedSelect();
        if (selected == 0) {
          continue;
        }
        selector.selectedKeys().forEach(this::handleKey);
        selector.selectedKeys().clear();
      }
    }
    catch (Exception e) {
      close();
      log.error("Stopping {} due to exception.", this, e);
    }
  }

  private int synchronizedSelect() throws IOException {
    int selected;
    synchronized (selectLock) {
      selected = selector.select();
    }
    synchronized (registerLock) {
    } // Wait until new channel is registered

    return selected;
  }

  public boolean isOpen() {
    return open.get();
  }

  public void close() {
    open.set(false);
  }

}
