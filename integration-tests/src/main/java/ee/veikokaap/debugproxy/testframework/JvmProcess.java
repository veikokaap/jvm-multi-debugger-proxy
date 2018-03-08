package ee.veikokaap.debugproxy.testframework;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

import kaap.veiko.debuggerforker.DebuggerForker;

public class JvmProcess {

  public static final int DEBUGGER_PORT = 16789;
  public static final int PROXY_PORT = 16456;

  private static final String JVM_EXECUTABLE = "java";

  private final Deque<String> outputDeque = new ConcurrentLinkedDeque<>();
  private final Process process;

  public static JvmProcess runClass(Class clazz) throws IOException {
    return new JvmProcess(startClassWithJvm(clazz));
  }

  private JvmProcess(Process process) {
    this.process = process;
    startDebugProxy();
    startVmOutputRouter();
  }

  private void startDebugProxy() {
    new Thread(() -> {
      try {
        DebuggerForker.create(new InetSocketAddress("127.0.0.1", DEBUGGER_PORT), PROXY_PORT).start();
      }
      catch (IOException e) {
        throw new RuntimeException(e);
      }
    }).start();
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

  public int waitForExit() throws InterruptedException {
    return process.waitFor();
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
    return "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=" + DEBUGGER_PORT;
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

}
