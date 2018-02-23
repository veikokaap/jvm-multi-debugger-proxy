package kaap.veiko.debuggerforker.commands.commandsets.eventrequest.filters;

import kaap.veiko.debuggerforker.types.DataReader;
import kaap.veiko.debuggerforker.types.DataWriter;
import kaap.veiko.debuggerforker.types.jdwp.Location;

public class EventRequestLocationFilter extends EventRequestFilter {
  public static final byte IDENTIFIER = 7;

  private final Location location;

  public EventRequestLocationFilter(DataReader dataReader) {
    this.location = Location.read(dataReader);
  }

  @Override
  public void write(DataWriter writer) {
    writer.writeByte(IDENTIFIER);
    writer.writeType(location);
  }

  public Location getLocation() {
    return location;
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
