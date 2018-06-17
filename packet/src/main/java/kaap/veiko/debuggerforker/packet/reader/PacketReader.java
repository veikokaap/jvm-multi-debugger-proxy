package kaap.veiko.debuggerforker.packet.reader;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import org.checkerframework.checker.nullness.qual.Nullable;

import kaap.veiko.debuggerforker.packet.Packet;
import kaap.veiko.debuggerforker.packet.PacketSource;

public class PacketReader {

  private final SocketChannel socketChannel;
  private final PacketSource source;

  private InProgressReadPacket inProgressReadPacket = new InProgressReadPacket();

  public PacketReader(SocketChannel socketChannel, PacketSource source) {
    this.socketChannel = socketChannel;
    this.source = source;
  }

  public @Nullable Packet read() throws IOException {
    inProgressReadPacket.read(socketChannel);

    byte[] bytes = inProgressReadPacket.getBytesIfFinishedReading();
    if (bytes != null) {
      inProgressReadPacket = new InProgressReadPacket();
      return PacketParser.parse(bytes, source);
    }

    return null;
  }


}
