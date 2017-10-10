package kaap.veiko.debuggerforker.commands.sets.eventrequest;


import kaap.veiko.debuggerforker.commands.Command;
import kaap.veiko.debuggerforker.commands.constants.EventKind;
import kaap.veiko.debuggerforker.commands.parser.annotations.JdwpCommand;
import kaap.veiko.debuggerforker.commands.parser.annotations.JdwpCommandConstructor;
import kaap.veiko.debuggerforker.commands.sets.CommandIdentifier;

@JdwpCommand(CommandIdentifier.CLEAR_EVENT_REQUEST_COMMAND)
public class ClearEventRequestCommand implements Command {
  private final EventKind eventKind;
  private final int requestId;

  @JdwpCommandConstructor
  public ClearEventRequestCommand(EventKind eventKind, int requestId) {
    this.eventKind = eventKind;
    this.requestId = requestId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ClearEventRequestCommand that = (ClearEventRequestCommand) o;

    if (requestId != that.requestId) {
      return false;
    }
    return eventKind == that.eventKind;
  }

  @Override
  public int hashCode() {
    int result = eventKind != null ? eventKind.hashCode() : 0;
    result = 31 * result + requestId;
    return result;
  }

  @Override
  public String toString() {
    return "ClearEventRequestCommand{" +
        "eventKind=" + eventKind +
        ", requestId=" + requestId +
        '}';
  }
}
