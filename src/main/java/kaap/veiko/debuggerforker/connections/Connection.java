package kaap.veiko.debuggerforker.connections;

import kaap.veiko.debuggerforker.packet.PacketStream;

public interface Connection extends AutoCloseable {
  PacketStream getPacketStream();
}
