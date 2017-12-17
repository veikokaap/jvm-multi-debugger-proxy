package kaap.veiko.debuggerforker.packet.internal;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import kaap.veiko.debuggerforker.packet.CommandPacket;
import kaap.veiko.debuggerforker.packet.Packet;
import kaap.veiko.debuggerforker.packet.PacketVisitor;
import kaap.veiko.debuggerforker.packet.ReplyPacket;

public class PacketTransformer {
  private final ConcurrentMap<Integer, Integer> idMap = new ConcurrentHashMap<>();
  private final AtomicInteger idCounter = new AtomicInteger(0);

  private final ReadPacketVisitor readPacketVisitor = new ReadPacketVisitor();
  private final WritePacketVisitor writePacketVisitor = new WritePacketVisitor();

  public Packet transformReadPacket(Packet packet) {
    return packet.visit(readPacketVisitor);
  }

  public Packet transformWritePacket(Packet packet) {
    return packet.visit(writePacketVisitor);
  }

  private int getNewId(int originalId) {
    int newId = idCounter.getAndIncrement();
    idMap.put(newId, originalId);
    return newId;
  }

  private int getOriginalId(int newId) {
    return idMap.get(newId);
  }

  private class ReadPacketVisitor implements PacketVisitor<Packet> {
    @Override
    public Packet visit(ReplyPacket packet) {
      int newId = getNewId(packet.getId());
      return new ReplyPacket(
          newId,
          packet.getErrorCode(),
          packet.getData(),
          packet.getSource()
      );
    }

    @Override
    public Packet visit(CommandPacket packet) {
      int newId = getNewId(packet.getId());
      return new CommandPacket(
          newId,
          packet.getCommandSetId(),
          packet.getCommandId(),
          packet.getData(),
          packet.getSource()
      );
    }
  }

  private class WritePacketVisitor implements PacketVisitor<Packet> {
    @Override
    public Packet visit(ReplyPacket packet) {
      int originalId = getOriginalId(packet.getId());
      return new ReplyPacket(
          originalId,
          packet.getErrorCode(),
          packet.getData(),
          packet.getSource()
      );
    }

    @Override
    public Packet visit(CommandPacket packet) {
      return packet;
    }
  }
}
