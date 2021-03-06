package kaap.veiko.debuggerforker.commands.commandsets.eventrequest;

import kaap.veiko.debuggerforker.commands.CommandBase;
import kaap.veiko.debuggerforker.commands.CommandVisitor;
import kaap.veiko.debuggerforker.commands.MutableCommandPacket;
import kaap.veiko.debuggerforker.commands.parser.CommandDataReader;
import kaap.veiko.debuggerforker.commands.commandsets.CommandIdentifier;
import kaap.veiko.debuggerforker.commands.util.CommandDataUtil;
import kaap.veiko.debuggerforker.packet.CommandPacket;
import kaap.veiko.debuggerforker.types.DataWriter;
import kaap.veiko.debuggerforker.types.VMInformation;
import kaap.veiko.debuggerforker.types.jdwp.EventKind;

public class ClearEventRequestCommand extends CommandBase<CommandPacket> {
  private static final CommandIdentifier COMMAND_IDENTIFIER = CommandIdentifier.CLEAR_EVENT_REQUEST_COMMAND;

  private final EventKind eventKind;
  private final int requestId;

  public static ClearEventRequestCommand create(int packetId, EventKind eventKind, int requestId, VMInformation vmInformation) {
    MutableCommandPacket packet = MutableCommandPacket.create(packetId, COMMAND_IDENTIFIER);
    ClearEventRequestCommand command = new ClearEventRequestCommand(packet, eventKind, requestId);
    packet.setData(CommandDataUtil.getCommandDataBytes(command, vmInformation));

    return command;
  }

  public static ClearEventRequestCommand read(CommandDataReader reader) {
    EventKind eventKind = EventKind.read(reader);
    int requestId = reader.readInt();

    return new ClearEventRequestCommand((CommandPacket) reader.getPacket(), eventKind, requestId);
  }

  private ClearEventRequestCommand(CommandPacket packet, EventKind eventKind, int requestId) {
    super(packet, COMMAND_IDENTIFIER);
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
  public void writeCommand(DataWriter writer) {
    writer.writeType(eventKind);
    writer.writeInt(requestId);
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
