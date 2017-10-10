package kaap.veiko.debuggerforker.packet;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class PacketIdTransformer {
  private final ConcurrentMap<Integer, Integer> idMap = new ConcurrentHashMap<>();
  private final AtomicInteger idCounter = new AtomicInteger(Integer.MIN_VALUE);

  public int getNewId(int originalId) {
    int newId = idCounter.getAndIncrement();
    idMap.put(newId, originalId);
    return newId;
  }

  public int getOriginalId(int newId) {
    return idMap.get(newId);
  }

}
