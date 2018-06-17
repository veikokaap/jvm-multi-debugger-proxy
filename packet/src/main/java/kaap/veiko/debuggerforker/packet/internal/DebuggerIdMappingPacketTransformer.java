package kaap.veiko.debuggerforker.packet.internal;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaap.veiko.debuggerforker.packet.CommandPacket;
import kaap.veiko.debuggerforker.packet.Packet;
import kaap.veiko.debuggerforker.packet.PacketVisitor;
import kaap.veiko.debuggerforker.packet.ReplyPacket;

public class DebuggerIdMappingPacketTransformer implements PacketTransformer {

  private static final Logger log = LoggerFactory.getLogger(DebuggerIdMappingPacketTransformer.class);

  private final ConcurrentMap<Integer, Integer> idMap = new ConcurrentHashMap<>();
  private final AtomicInteger idCounter = new AtomicInteger(0);

  private final OutputPacketVisitor outputPacketVisitor = new OutputPacketVisitor();
  private final InputPacketVisitor inputPacketVisitor = new InputPacketVisitor();

  @Override
  public Packet transformReadPacket(Packet packet) {
    return packet.visit(outputPacketVisitor);
  }

  @Override
  public Packet transformWritePacket(Packet packet) {
    return packet.visit(inputPacketVisitor);
  }

  @Override
  public int createNewId() {
    return idCounter.getAndIncrement();
  }

  private int getNewId(int originalId) {
    int newId = idCounter.getAndIncrement();
    idMap.put(newId, originalId);
    return newId;
  }

  private int getOriginalId(int newId) {
    Integer originalId = idMap.remove(newId);
    if (originalId == null) {
      throw new IllegalStateException("No original Id found for mapped id " + newId);
    }
    return originalId;
  }

  private class OutputPacketVisitor implements PacketVisitor<Packet> {
    @Override
    public Packet visit(ReplyPacket packet) {
      log.error("Debugger shouldn't output reply packets");
      return packet;
    }

    @Override
    public Packet visit(CommandPacket packet) {
      // Generate new id to ensure every packet reaching vm has a unique id
      // This also makes it easy to later connect reply and command packets and avoid collision
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

  private class InputPacketVisitor implements PacketVisitor<Packet> {
    @Override
    public Packet visit(ReplyPacket packet) {
      // Restore original id
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
      // id of this packet should already be changed by the vm packet transformer
      return packet;
    }
  }
}
