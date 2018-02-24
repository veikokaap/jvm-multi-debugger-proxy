package kaap.veiko.debuggerforker.commands.commandsets.event.events;

import kaap.veiko.debuggerforker.types.DataReader;
import kaap.veiko.debuggerforker.types.DataWriter;
import kaap.veiko.debuggerforker.types.jdwp.Location;
import kaap.veiko.debuggerforker.types.jdwp.ThreadId;

public class MethodEntryEvent extends VirtualMachineEvent {

  private final int requestId;
  private final ThreadId thread;
  private final Location location;

  public static MethodEntryEvent read(DataReader reader) {
    return new MethodEntryEvent(
        reader.readInt(),
        ThreadId.read(reader),
        Location.read(reader)
    );
  }

  public MethodEntryEvent(int requestId, ThreadId thread, Location location) {
    this.requestId = requestId;
    this.thread = thread;
    this.location = location;
  }

  @Override
  public void write(DataWriter writer) {
    writer.writeInt(requestId);
    writer.writeType(thread);
    writer.writeType(location);
  }

  public int getRequestId() {
    return requestId;
  }

  public ThreadId getThread() {
    return thread;
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

    MethodEntryEvent that = (MethodEntryEvent) o;

    if (requestId != that.requestId) {
      return false;
    }
    if (thread != null ? !thread.equals(that.thread) : that.thread != null) {
      return false;
    }
    return location != null ? location.equals(that.location) : that.location == null;
  }

  @Override
  public int hashCode() {
    int result = requestId;
    result = 31 * result + (thread != null ? thread.hashCode() : 0);
    result = 31 * result + (location != null ? location.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "MethodEntryEvent{" +
        "requestId=" + requestId +
        ", thread=" + thread +
        ", location=" + location +
        '}';
  }
}
