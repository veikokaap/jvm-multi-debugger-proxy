package kaap.veiko.debuggerforker.packet;

import static kaap.veiko.debuggerforker.packet.PacketSource.SourceType.DEBUGGER;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import kaap.veiko.debuggerforker.packet.internal.PacketTransformer;
import kaap.veiko.debuggerforker.packet.internal.PacketStreamBase;

public class DebuggerPacketStream extends PacketStreamBase {

  public DebuggerPacketStream(SocketChannel socketChannel, PacketTransformer packetTransformer) throws IOException {
    super(socketChannel, DEBUGGER, packetTransformer);
  }

  @Override
  public String toString() {
    return "DebuggerPacketStream{" +
        "socketChannel=" + getSocketChannel() +
        '}';
  }
}
