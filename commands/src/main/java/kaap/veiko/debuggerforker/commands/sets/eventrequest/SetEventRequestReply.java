package kaap.veiko.debuggerforker.commands.sets.eventrequest;


import kaap.veiko.debuggerforker.commands.CommandBase;
import kaap.veiko.debuggerforker.commands.parser.CommandDataWriter;
import kaap.veiko.debuggerforker.commands.sets.CommandIdentifier;
import kaap.veiko.debuggerforker.types.DataReader;

public class SetEventRequestReply extends CommandBase {
  public static final CommandIdentifier COMMAND_IDENTIFIER = CommandIdentifier.SET_EVENT_REQUEST_REPLY;

  private final int requestId;

  public SetEventRequestReply(DataReader reader) {
    this.requestId = reader.readInt();
  }

  @Override
  protected void writeCommand(CommandDataWriter writer) {
    writer.writeInt(requestId);
  }

  @Override
  protected CommandIdentifier getCommandIdentifier() {
    return COMMAND_IDENTIFIER;
  }

  public int getRequestId() {
    return requestId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    SetEventRequestReply that = (SetEventRequestReply) o;

    return requestId == that.requestId;
  }

  @Override
  public int hashCode() {
    return requestId;
  }

  @Override
  public String toString() {
    return "SetEventRequestReply{" +
        "requestId=" + requestId +
        '}';
  }
}
