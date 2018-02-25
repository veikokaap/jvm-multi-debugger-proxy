package kaap.veiko.debuggerforker.packet;

import java.nio.channels.SocketChannel;

import kaap.veiko.debuggerforker.packet.internal.PacketTransformer;

public class PacketSource {

  private final SocketChannel socketChannel;
  private final SourceType sourceType;
  private final PacketTransformer packetTransformer;

  private boolean holdEvents;

  public PacketSource(SocketChannel socketChannel, SourceType sourceType, PacketTransformer packetTransformer) {
    this.socketChannel = socketChannel;
    this.sourceType = sourceType;
    this.packetTransformer = packetTransformer;
  }

  public int createNewId() {
    return packetTransformer.createNewId();
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
    return sourceType.toString();
  }

  public void setHoldEvents(boolean holdEvents) {
    this.holdEvents = holdEvents;
  }

  public boolean isHoldEvents() {
    return holdEvents;
  }

  public enum SourceType {
    DEBUGGER,
    VIRTUAL_MACHINE
  }
}
