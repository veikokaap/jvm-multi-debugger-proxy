package kaap.veiko.debuggerforker;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Main {

  private static final String JVM_HOST_ARG = "jvm.host=";
  private static final String JVM_PORT_ARG = "jvm.port=";
  private static final String PROXY_PORT_ARG = "proxy.port=";

  public static void main(String[] args) throws IOException {
    List<String> arguments = Arrays.asList(args);

    InetSocketAddress virtualMachineAddress = getJvmAddress(arguments);
    Integer proxyPort = getProxyPort(arguments);

    if (virtualMachineAddress == null || proxyPort == null) {
      return;
    }

    DebuggerProxy proxy = new DebuggerProxy(virtualMachineAddress, proxyPort);
    proxy.start();
  }

  private static InetSocketAddress getJvmAddress(List<String> args) {
    String host = args.stream()
        .filter(s -> s.startsWith(JVM_HOST_ARG))
        .map(s -> s.substring(JVM_HOST_ARG.length()))
        .findFirst()
        .orElse("127.0.0.1");

    Optional<String> port = args.stream()
        .filter(s -> s.startsWith(JVM_PORT_ARG))
        .map(s -> s.substring(JVM_PORT_ARG.length()))
        .findFirst();

    if (port.isPresent()) {
      try {
        int jvmPort = Integer.parseInt(port.get());
        return new InetSocketAddress(host, jvmPort);
      }
      catch (NumberFormatException e) {
        System.err.println("JVM debug port is not a valid number");
        return null;
      }
    }
    else {
      System.err.println("No JVM debug port specified. Please add " + JVM_PORT_ARG + "<port> argument.");
      return null;
    }
  }

  private static Integer getProxyPort(List<String> args) {
    Optional<String> port = args.stream()
        .filter(s -> s.startsWith(PROXY_PORT_ARG))
        .map(s -> s.substring(PROXY_PORT_ARG.length()))
        .findFirst();

    if (port.isPresent()) {
      try {
        return Integer.parseInt(port.get());
      }
      catch (NumberFormatException e) {
        System.err.println("Proxy server port is not a valid number");
        return null;
      }
    }
    else {
      System.err.println("No proxy server port specified. Please add -D" + PROXY_PORT_ARG + "<port> JVM argument.");
      return null;
    }
  }
}
