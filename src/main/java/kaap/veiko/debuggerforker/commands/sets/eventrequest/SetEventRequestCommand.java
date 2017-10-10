package kaap.veiko.debuggerforker.commands.sets.eventrequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kaap.veiko.debuggerforker.commands.Command;
import kaap.veiko.debuggerforker.commands.constants.EventKind;
import kaap.veiko.debuggerforker.commands.parser.annotations.JdwpArray;
import kaap.veiko.debuggerforker.commands.parser.annotations.JdwpCommand;
import kaap.veiko.debuggerforker.commands.parser.annotations.JdwpCommandConstructor;
import kaap.veiko.debuggerforker.commands.sets.CommandIdentifier;
import kaap.veiko.debuggerforker.commands.sets.eventrequest.filters.EventRequestFilter;

@JdwpCommand(CommandIdentifier.SET_EVENT_REQUEST_COMMAND)
public class SetEventRequestCommand implements Command {
  private final EventKind eventKind;
  private final byte suspendPolicy;
  private final List<EventRequestFilter> eventRequestFilters;

  @JdwpCommandConstructor
  public SetEventRequestCommand(EventKind eventKind, byte suspendPolicy, @JdwpArray(counterType = int.class) EventRequestFilter[] eventRequestFilters) {
    this.eventKind = eventKind;
    this.suspendPolicy = suspendPolicy;
    this.eventRequestFilters = new ArrayList<>(Arrays.asList(eventRequestFilters));
  }

  public EventKind getEventKind() {
    return eventKind;
  }

  public byte getSuspendPolicy() {
    return suspendPolicy;
  }

  public List<EventRequestFilter> getEventRequestFilters() {
    return eventRequestFilters;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    SetEventRequestCommand that = (SetEventRequestCommand) o;

    if (suspendPolicy != that.suspendPolicy) {
      return false;
    }
    if (eventKind != that.eventKind) {
      return false;
    }
    return eventRequestFilters != null ? eventRequestFilters.equals(that.eventRequestFilters) : that.eventRequestFilters == null;
  }

  @Override
  public int hashCode() {
    int result = eventKind != null ? eventKind.hashCode() : 0;
    result = 31 * result + (int) suspendPolicy;
    result = 31 * result + (eventRequestFilters != null ? eventRequestFilters.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "SetEventRequestCommand{" +
        "eventKind=" + eventKind +
        ", suspendPolicy=" + suspendPolicy +
        ", eventRequestFilters=" + eventRequestFilters +
        '}';
  }
}