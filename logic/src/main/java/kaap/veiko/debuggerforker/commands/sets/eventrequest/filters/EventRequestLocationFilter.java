package kaap.veiko.debuggerforker.commands.sets.eventrequest.filters;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.commands.parser.annotations.JdwpCommandConstructor;
import kaap.veiko.debuggerforker.types.Location;

@EventFilterKind(value = 7)
public class EventRequestLocationFilter extends EventRequestFilter {
  private final Location location;

  @JdwpCommandConstructor
  public EventRequestLocationFilter(Location location) {
    this.location = location;
  }

  public Location getLocation() {
    return location;
  }

  @Override
  public void putToBuffer(ByteBuffer buffer) {
    location.putToBuffer(buffer);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    EventRequestLocationFilter that = (EventRequestLocationFilter) o;

    return location != null ? location.equals(that.location) : that.location == null;
  }

  @Override
  public int hashCode() {
    return location != null ? location.hashCode() : 0;
  }

  @Override
  public String toString() {
    return "EventRequestLocationFilter{" +
        "location=" + location +
        '}';
  }
}
