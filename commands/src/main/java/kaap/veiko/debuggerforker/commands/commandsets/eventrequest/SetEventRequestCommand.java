package kaap.veiko.debuggerforker.commands.commandsets.eventrequest;

import java.util.List;

import kaap.veiko.debuggerforker.commands.CommandBase;
import kaap.veiko.debuggerforker.commands.CommandVisitor;
import kaap.veiko.debuggerforker.commands.MutableCommandPacket;
import kaap.veiko.debuggerforker.commands.parser.CommandDataReader;
import kaap.veiko.debuggerforker.commands.parser.CommandDataWriter;
import kaap.veiko.debuggerforker.commands.commandsets.CommandIdentifier;
import kaap.veiko.debuggerforker.commands.commandsets.eventrequest.filters.EventRequestFilter;
import kaap.veiko.debuggerforker.commands.util.CommandDataUtil;
import kaap.veiko.debuggerforker.packet.Packet;
import kaap.veiko.debuggerforker.types.VMInformation;
import kaap.veiko.debuggerforker.types.jdwp.EventKind;

public class SetEventRequestCommand extends CommandBase {
  private static final CommandIdentifier COMMAND_IDENTIFIER = CommandIdentifier.SET_EVENT_REQUEST_COMMAND;

  private final EventKind eventKind;
  private final byte suspendPolicy;
  private final List<EventRequestFilter> eventRequestFilters;

  private SetEventRequestReply eventRequestReply;

  public static SetEventRequestCommand create(int packetId, VMInformation vmInformation, EventKind eventKind, byte suspendPolicy, List<EventRequestFilter> eventRequestFilters) {
    MutableCommandPacket packet = MutableCommandPacket.create(packetId, COMMAND_IDENTIFIER);
    SetEventRequestCommand command = new SetEventRequestCommand(packet, eventKind, suspendPolicy, eventRequestFilters);
    packet.setData(CommandDataUtil.getCommandDataBytes(command, vmInformation));

    return command;
  }

  public static SetEventRequestCommand read(CommandDataReader reader) {
    EventKind eventKind = EventKind.read(reader);
    byte suspendPolicy = reader.readByte();
    List<EventRequestFilter> eventRequestFilters = reader.readList(EventRequestFilter.PARSER);

    return new SetEventRequestCommand(reader.getPacket(), eventKind, suspendPolicy, eventRequestFilters);
  }

  private SetEventRequestCommand(Packet packet, EventKind eventKind, byte suspendPolicy, List<EventRequestFilter> eventRequestFilters) {
    super(packet, COMMAND_IDENTIFIER);
    this.eventKind = eventKind;
    this.suspendPolicy = suspendPolicy;
    this.eventRequestFilters = eventRequestFilters;
  }

  @Override
  public void writeCommand(CommandDataWriter writer) {
    writer.writeType(eventKind);
    writer.writeByte(suspendPolicy);
    writer.writeList(EventRequestFilter.PARSER, eventRequestFilters);
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
  public void visit(CommandVisitor visitor) {
    visitor.visit(this);
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
