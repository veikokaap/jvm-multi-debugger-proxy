package kaap.veiko.debuggerforker.commands.sets.eventrequest;

import java.util.List;

import kaap.veiko.debuggerforker.commands.CommandBase;
import kaap.veiko.debuggerforker.commands.CommandVisitor;
import kaap.veiko.debuggerforker.commands.parser.CommandDataReader;
import kaap.veiko.debuggerforker.commands.parser.CommandDataWriter;
import kaap.veiko.debuggerforker.commands.sets.CommandIdentifier;
import kaap.veiko.debuggerforker.commands.sets.eventrequest.filters.EventRequestFilter;
import kaap.veiko.debuggerforker.packet.Packet;
import kaap.veiko.debuggerforker.types.jdwp.EventKind;

public class SetEventRequestCommand extends CommandBase {
  public static final CommandIdentifier COMMAND_IDENTIFIER = CommandIdentifier.SET_EVENT_REQUEST_COMMAND;

  private final EventKind eventKind;
  private final byte suspendPolicy;
  private final List<EventRequestFilter> eventRequestFilters;

  private SetEventRequestReply eventRequestReply;

  public SetEventRequestCommand(CommandDataReader reader, Packet packet) {
    super(packet);
    this.eventKind = reader.readType(EventKind.class);
    this.suspendPolicy = reader.readByte();
    this.eventRequestFilters = reader.readList(EventRequestFilter.PARSER);
  }

  @Override
  protected void writeCommand(CommandDataWriter writer) {
    writer.writeType(eventKind);
    writer.writeByte(suspendPolicy);
    writer.writeList(EventRequestFilter.PARSER, eventRequestFilters);
  }

  @Override
  protected CommandIdentifier getCommandIdentifier() {
    return COMMAND_IDENTIFIER;
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

  public SetEventRequestReply getEventRequestReply() {
    return eventRequestReply;
  }

  public void setEventRequestReply(SetEventRequestReply eventRequestReply) {
    this.eventRequestReply = eventRequestReply;
  }

  @Override
  public <T> T visit(CommandVisitor<T> visitor) {
    return visitor.visit(this);
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
