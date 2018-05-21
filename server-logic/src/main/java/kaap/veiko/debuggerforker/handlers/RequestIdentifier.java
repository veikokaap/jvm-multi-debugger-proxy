package kaap.veiko.debuggerforker.handlers;

import org.checkerframework.checker.nullness.qual.Nullable;

import kaap.veiko.debuggerforker.types.jdwp.EventKind;

final public class RequestIdentifier {
  private final EventKind eventKind;
  private final int requestId;

  public RequestIdentifier(EventKind eventKind, int requestId) {
    this.eventKind = eventKind;
    this.requestId = requestId;
  }

  public EventKind getEventKind() {
    return eventKind;
  }

  public int getRequestId() {
    return requestId;
  }

  @Override
  public boolean equals(@Nullable Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    RequestIdentifier requestIdentifier = (RequestIdentifier) o;

    if (requestId != requestIdentifier.requestId) {
      return false;
    }
    return eventKind == requestIdentifier.eventKind;
  }

  @Override
  public int hashCode() {
    int result = eventKind != null ? eventKind.hashCode() : 0;
    result = 31 * result + requestId;
    return result;
  }

  @Override
  public String toString() {
    return "RequestIdentifier{" +
        "eventKind=" + eventKind +
        ", requestId=" + requestId +
        '}';
  }
}
