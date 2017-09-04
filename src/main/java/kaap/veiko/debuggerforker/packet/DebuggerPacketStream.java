package kaap.veiko.debuggerforker.packet;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class DebuggerPacketStream extends PacketStreamBase {

  private static final ConcurrentMap<Integer, Integer> idMap = new ConcurrentHashMap<>();
  private static final AtomicInteger idCounter = new AtomicInteger(Integer.MIN_VALUE);
  private final List<Integer> thisNewIds = new ArrayList<>();

  public DebuggerPacketStream(SocketChannel socketChannel) throws IOException {
    super(socketChannel, false);
  }

  @Override
  public Packet read() throws IOException {
    Packet packet = super.read();
    if (packet == null) {
      return null;
    }

    int originalId = packet.getId();
    int newId = idCounter.getAndIncrement();

    thisNewIds.add(newId);
    idMap.put(newId, originalId);

    packet.setId(newId);

    return packet;
  }

  @Override
  public void write(Packet packet) throws IOException {
    if (packet.isReply()) {
      int originalId = idMap.get(packet.getId());
      packet.setId(originalId);
    }

    super.write(packet);
  }

  public boolean isMyReply(Packet packet) {
    return thisNewIds.contains(packet.getId());
  }
}
