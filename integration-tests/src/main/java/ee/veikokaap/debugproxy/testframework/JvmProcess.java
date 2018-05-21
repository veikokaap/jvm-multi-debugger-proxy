package ee.veikokaap.debugproxy.testframework;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;

import org.checkerframework.checker.initialization.qual.UnderInitialization;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;

import kaap.veiko.debuggerforker.DebuggerProxy;

public class JvmProcess implements Closeable {

  public static final int DEBUGGER_PORT = 16789;
  public static final int PROXY_PORT = 16456;

  private static final String JVM_EXECUTABLE = "java";

  private final Deque<String> outputDeque = new ConcurrentLinkedDeque<>();
  private final Process process;
  private final Thread proxyThread;
  private @MonotonicNonNull DebuggerProxy debuggerProxy = null;

  public static JvmProcess runClass(Class clazz) throws IOException {
    JvmProcess process = new JvmProcess(startClassWithJvm(clazz));
    process.start();
    return process;
  }

  private JvmProcess(Process process) {
    this.process = process;
    this.proxyThread = new Thread(() -> {
      try {
        debuggerProxy = new DebuggerProxy(new InetSocketAddress("127.0.0.1", DEBUGGER_PORT), PROXY_PORT);
        debuggerProxy.start();
      }
      catch (IOException e) {
        throw new RuntimeException(e);
      }
    });
  }

  private void start() {
    startDebugProxy();
    startVmOutputRouter();
  }

  private void startDebugProxy() {
    proxyThread.start();
    try {
      Thread.sleep(500);
    }
    catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private void startVmOutputRouter() {
    new Thread(this::routeOutput).start();
  }

  public Deque<String> getOutputDeque() {
    return outputDeque;
  }

  /**
   * @throws InterruptedException
   */
  public void waitForExit() throws InterruptedException {
    process.waitFor();
  }

  private void stopProxyAndWait() throws InterruptedException {
    if (debuggerProxy != null) {
      debuggerProxy.stop();
      proxyThread.join();
    }
  }

  private static Process startClassWithJvm(Class clazz) throws IOException {
    return new ProcessBuilder()
        .command(buildCommand(clazz))
        .redirectErrorStream(true)
        .start();
  }

  private static String[] buildCommand(Class clazz) {
    return new String[]{
        JVM_EXECUTABLE,
        "-cp", getCurrentClasspath(),
        getDebuggerArgument(),
        clazz.getName()
    };
  }

  private static String getCurrentClasspath() {
    return System.getProperty("java.class.path");
  }

  private static String getDebuggerArgument() {
    return "-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=" + DEBUGGER_PORT;
  }

  private void routeOutput() {
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
      String line;
      while ((line = reader.readLine()) != null) {
        outputDeque.addLast(line);
      }
    }
    catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  @Override
  public void close() {
    try {
      if (process.isAlive()) {
        process.destroy();
        process.waitFor(2, TimeUnit.SECONDS);
        process.destroyForcibly();
        throw new AssertionError("JVM failed to exit on its own");
      }
    }
    catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
    finally {
      try {
        stopProxyAndWait();
      }
      catch (InterruptedException ignored) {
      }
    }
  }

}
