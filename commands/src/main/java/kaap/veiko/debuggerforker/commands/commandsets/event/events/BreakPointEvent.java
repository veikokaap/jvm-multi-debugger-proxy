package kaap.veiko.debuggerforker.commands.commandsets.event.events;

import kaap.veiko.debuggerforker.types.DataReader;
import kaap.veiko.debuggerforker.types.DataWriter;
import kaap.veiko.debuggerforker.types.jdwp.EventKind;
import kaap.veiko.debuggerforker.types.jdwp.Location;
import kaap.veiko.debuggerforker.types.jdwp.ThreadId;

public class BreakPointEvent extends VirtualMachineEvent {
  public static final EventKind EVENT_KIND_IDENTIFIER = EventKind.BREAKPOINT;

  private int requestId;
  private ThreadId threadId;
  private Location location;

  public BreakPointEvent(DataReader reader) {
    this.requestId = reader.readInt();
    this.threadId = ThreadId.read(reader);
    this.location = Location.read(reader);
  }

  @Override
  public void write(DataWriter writer) {
    writer.writeType(EVENT_KIND_IDENTIFIER);
    writer.writeInt(requestId);
    writer.writeType(threadId);
    writer.writeType(location);
  }

  public int getRequestId() {
    return requestId;
  }

  public ThreadId getThreadId() {
    return threadId;
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

    BreakPointEvent that = (BreakPointEvent) o;

    if (requestId != that.requestId) {
      return false;
    }
    if (threadId != null ? !threadId.equals(that.threadId) : that.threadId != null) {
      return false;
    }
    return location != null ? location.equals(that.location) : that.location == null;
  }

  @Override
  public int hashCode() {
    int result = requestId;
    result = 31 * result + (threadId != null ? threadId.hashCode() : 0);
    result = 31 * result + (location != null ? location.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "BreakPointEvent{" +
        "requestId=" + requestId +
        ", threadId=" + threadId +
        ", location=" + location +
        '}';
  }
}
