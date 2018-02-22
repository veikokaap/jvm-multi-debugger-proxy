package kaap.veiko.debuggerforker.packet;

import java.nio.channels.SocketChannel;

public class PacketSource {

  private final SocketChannel socketChannel;
  private final SourceType sourceType;

  public PacketSource(SocketChannel socketChannel, SourceType sourceType) {
    this.socketChannel = socketChannel;
    this.sourceType = sourceType;
  }

  public boolean isDebugger() {
    return sourceType == SourceType.DEBUGGER;
  }

  public boolean isVirtualMachine() {
    return sourceType == SourceType.VIRTUAL_MACHINE;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PacketSource that = (PacketSource) o;

    return socketChannel != null ? socketChannel.equals(that.socketChannel) : that.socketChannel == null;
  }

  @Override
  public int hashCode() {
    return socketChannel != null ? socketChannel.hashCode() : 0;
  }

  @Override
  public String toString() {
    return "PacketSource{" +
        "socketChannel=" + socketChannel +
        ", sourceType=" + sourceType +
        '}';
  }

  public enum SourceType {
    DEBUGGER,
    VIRTUAL_MACHINE;
  }
}