package kaap.veiko.debuggerforker.events;

import kaap.veiko.debuggerforker.commands.constants.EventKind;
import kaap.veiko.debuggerforker.commands.parser.annotations.JdwpCommandConstructor;
import kaap.veiko.debuggerforker.commands.types.Location;
import kaap.veiko.debuggerforker.commands.types.ThreadId;

@JdwpEvent(EventKind.BREAKPOINT)
public class BreakPointEvent {
  private int requestId;
  private ThreadId threadId;
  private Location location;

  @JdwpCommandConstructor
  public BreakPointEvent(int requestId, ThreadId threadId, Location location) {
    this.requestId = requestId;
    this.threadId = threadId;
    this.location = location;
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
