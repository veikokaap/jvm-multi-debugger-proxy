package ee.veikokaap.debugproxy.testframework;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;

import kaap.veiko.debuggerforker.DebuggerProxy;

public class JvmProcess implements Closeable {

  public static final int DEBUGGER_PORT = 16789;
  public static final int PROXY_PORT = 16456;

  private static final String JVM_EXECUTABLE = "java";

  private final Deque<String> outputDeque = new ConcurrentLinkedDeque<>();
  private final Process process;
  private final Thread proxyThread;
  private DebuggerProxy debuggerProxy;

  public static JvmProcess runClass(Class clazz) throws IOException {
    return new JvmProcess(startClassWithJvm(clazz));
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
   *
   * @param timeout the maximum time to wait
   * @param unit the time unit of the {@code timeout} argument
   * @return {@code true} if the subprocess has exited and {@code false} if
   *         the waiting time elapsed before the subprocess has exited.
   * @throws InterruptedException
   */
  public void waitForExit(long timeout, TimeUnit unit) throws InterruptedException {
    boolean exited = process.waitFor(timeout, unit);
    if (!exited) {
      process.destroy();
      process.waitFor(2, TimeUnit.SECONDS);
      process.destroyForcibly();
      throw new AssertionError("JVM didn't exit on its own");
    }
    stopProxyAndWait();
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
      waitForExit(1, TimeUnit.SECONDS);
    }
    catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }
}
