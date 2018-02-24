package kaap.veiko.debuggerforker.commands.commandsets.event.events;

import kaap.veiko.debuggerforker.types.DataReader;
import kaap.veiko.debuggerforker.types.DataWriter;
import kaap.veiko.debuggerforker.types.jdwp.Location;
import kaap.veiko.debuggerforker.types.jdwp.ThreadId;

public class SingleStepEvent extends VirtualMachineEvent {

  private final int requestId;
  private final ThreadId thread;
  private final Location location;

  public static SingleStepEvent read(DataReader reader) {
    return new SingleStepEvent(reader);
  }

  private SingleStepEvent(DataReader reader) {
    requestId = reader.readInt();
    thread = ThreadId.read(reader);
    location = Location.read(reader);
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

    SingleStepEvent that = (SingleStepEvent) o;

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
    return "SingleStepEvent{" +
        "requestId=" + requestId +
        ", thread=" + thread +
        ", location=" + location +
        '}';
  }
}
