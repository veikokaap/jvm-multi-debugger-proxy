package kaap.veiko.debuggerforker.handlers;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import kaap.veiko.debuggerforker.packet.PacketSource;
import kaap.veiko.debuggerforker.types.jdwp.Location;

public class Breakpoint {

  private final Location location;
  private final List<PacketSource> sources = new CopyOnWriteArrayList<>();

  private int requestId;

  public Breakpoint(Location location) {
    this.location = location;
  }

  public void setRequestId(int requestId) {
    this.requestId = requestId;
  }

  public List<PacketSource> getSources() {
    return sources;
  }

  public Location getLocation() {
    return location;
  }

  public int getRequestId() {
    return requestId;
  }
}
