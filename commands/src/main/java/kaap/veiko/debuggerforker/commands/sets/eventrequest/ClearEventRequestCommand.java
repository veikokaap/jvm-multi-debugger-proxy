package kaap.veiko.debuggerforker.commands.sets.eventrequest;


import kaap.veiko.debuggerforker.commands.CommandBase;
import kaap.veiko.debuggerforker.commands.CommandVisitor;
import kaap.veiko.debuggerforker.commands.parser.CommandDataWriter;
import kaap.veiko.debuggerforker.commands.sets.CommandIdentifier;
import kaap.veiko.debuggerforker.packet.Packet;
import kaap.veiko.debuggerforker.types.DataReader;
import kaap.veiko.debuggerforker.types.jdwp.EventKind;

public class ClearEventRequestCommand extends CommandBase {
  public static final CommandIdentifier COMMAND_IDENTIFIER = CommandIdentifier.CLEAR_EVENT_REQUEST_COMMAND;

  private final EventKind eventKind;
  private final int requestId;

  public ClearEventRequestCommand(DataReader reader, Packet packet) {
    super(packet);
    this.eventKind = reader.readType(EventKind.class);
    this.requestId = reader.readInt();
  }

  @Override
  protected void writeCommand(CommandDataWriter writer) {
    writer.writeType(eventKind);
    writer.writeInt(requestId);
  }

  @Override
  protected CommandIdentifier getCommandIdentifier() {
    return COMMAND_IDENTIFIER;
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
