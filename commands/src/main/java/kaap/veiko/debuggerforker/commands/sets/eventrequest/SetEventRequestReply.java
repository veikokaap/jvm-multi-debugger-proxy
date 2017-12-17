package kaap.veiko.debuggerforker.commands.sets.eventrequest;


import kaap.veiko.debuggerforker.commands.CommandBase;
import kaap.veiko.debuggerforker.commands.CommandVisitor;
import kaap.veiko.debuggerforker.commands.MutableReplyPacket;
import kaap.veiko.debuggerforker.commands.parser.CommandDataReader;
import kaap.veiko.debuggerforker.commands.parser.CommandDataWriter;
import kaap.veiko.debuggerforker.commands.sets.CommandIdentifier;
import kaap.veiko.debuggerforker.commands.util.CommandDataUtil;
import kaap.veiko.debuggerforker.packet.Packet;
import kaap.veiko.debuggerforker.types.VMInformation;

public class SetEventRequestReply extends CommandBase {
  private static final CommandIdentifier COMMAND_IDENTIFIER = CommandIdentifier.SET_EVENT_REQUEST_REPLY;

  private final int requestId;

  public static SetEventRequestReply create(int packetId, VMInformation vmInformation, int requestId) {
    MutableReplyPacket packet = MutableReplyPacket.create(packetId);
    SetEventRequestReply command = new SetEventRequestReply(packet, requestId);
    packet.setData(CommandDataUtil.getCommandDataBytes(command, vmInformation));

    return command;
  }

  public static SetEventRequestReply read(CommandDataReader reader) {
    int requestId = reader.readInt();
    return new SetEventRequestReply(reader.getPacket(), requestId);
  }

  private SetEventRequestReply(Packet packet, int requestId) {
    super(packet, COMMAND_IDENTIFIER);
    this.requestId = requestId;
  }

  @Override
  public void writeCommand(CommandDataWriter writer) {
    writer.writeInt(requestId);
  }

  public int getRequestId() {
    return requestId;
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
