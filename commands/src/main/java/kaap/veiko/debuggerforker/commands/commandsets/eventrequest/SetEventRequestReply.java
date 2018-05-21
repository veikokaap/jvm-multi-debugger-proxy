package kaap.veiko.debuggerforker.commands.commandsets.eventrequest;

import org.checkerframework.checker.nullness.qual.Nullable;

import kaap.veiko.debuggerforker.commands.CommandBase;
import kaap.veiko.debuggerforker.commands.CommandVisitor;
import kaap.veiko.debuggerforker.commands.MutableReplyPacket;
import kaap.veiko.debuggerforker.commands.parser.CommandDataReader;
import kaap.veiko.debuggerforker.commands.commandsets.CommandIdentifier;
import kaap.veiko.debuggerforker.commands.util.CommandDataUtil;
import kaap.veiko.debuggerforker.packet.ReplyPacket;
import kaap.veiko.debuggerforker.types.DataWriter;
import kaap.veiko.debuggerforker.types.VMInformation;

public class SetEventRequestReply extends CommandBase<ReplyPacket> {
  private static final CommandIdentifier COMMAND_IDENTIFIER = CommandIdentifier.SET_EVENT_REQUEST_REPLY;

  private final int requestId;

  public static SetEventRequestReply create(int packetId, int requestId, VMInformation vmInformation) {
    MutableReplyPacket packet = MutableReplyPacket.create(packetId);
    SetEventRequestReply command = new SetEventRequestReply(packet, requestId);
    packet.setData(CommandDataUtil.getCommandDataBytes(command, vmInformation));

    return command;
  }

  public static SetEventRequestReply read(CommandDataReader reader) {
    int requestId = reader.readInt();
    return new SetEventRequestReply((ReplyPacket) reader.getPacket(), requestId);
  }

  private SetEventRequestReply(ReplyPacket packet, int requestId) {
    super(packet, COMMAND_IDENTIFIER);
    this.requestId = requestId;
  }

  @Override
  public void writeCommand(DataWriter writer) {
    writer.writeInt(requestId);
  }

  public int getRequestId() {
    return requestId;
  }

  @Override
  public void visit(CommandVisitor visitor) {
    visitor.visit(this);
  }

  @Override
  public boolean equals(@Nullable Object o) {
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
