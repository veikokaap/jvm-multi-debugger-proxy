package kaap.veiko.debuggerforker.packet.internal;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import kaap.veiko.debuggerforker.packet.CommandPacket;
import kaap.veiko.debuggerforker.packet.Packet;
import kaap.veiko.debuggerforker.packet.ReplyPacket;

public class PacketTransformer {
  private final ConcurrentMap<Integer, Integer> idMap = new ConcurrentHashMap<>();
  private final AtomicInteger idCounter = new AtomicInteger(0);

  public Packet transformReadPacket(Packet packet) {
    int originalId = packet.getId();
    int newId = getNewId(originalId);

    if (packet.isReply()) {
      ReplyPacket replyPacket = (ReplyPacket) packet;
      return new ReplyPacketImpl(
          replyPacket.getLength(),
          newId,
          replyPacket.getFlags(),
          replyPacket.getErrorCode(),
          replyPacket.getDataBytes(),
          replyPacket.getSource()
      );
    } else {
      CommandPacket commandPacket = (CommandPacket) packet;
      return new CommandPacketImpl(
          commandPacket.getLength(),
          newId,
          commandPacket.getFlags(),
          commandPacket.getCommandSetId(),
          commandPacket.getCommandId(),
          commandPacket.getDataBytes(),
          commandPacket.getSource()
      );
    }
  }

  public Packet transformWritePacket(Packet packet) {
    if (packet.isReply()) {
      ReplyPacket replyPacket = (ReplyPacket) packet;
      int originalId = getOriginalId(packet.getId());
      return new ReplyPacketImpl(
          replyPacket.getLength(),
          originalId,
          replyPacket.getFlags(),
          replyPacket.getErrorCode(),
          replyPacket.getDataBytes(),
          replyPacket.getSource()
      );
    }

    return packet;
  }

  private int getNewId(int originalId) {
    int newId = idCounter.getAndIncrement();
    idMap.put(newId, originalId);
    return newId;
  }

  private int getOriginalId(int newId) {
    return idMap.get(newId);
  }
}
