package kaap.veiko.debuggerforker.packet.internal;

import kaap.veiko.debuggerforker.packet.Packet;

public interface PacketTransformer {
  Packet transformReadPacket(Packet packet);

  Packet transformWritePacket(Packet packet);

  int createNewId();
}
