package kaap.veiko.debuggerforker.commands.commandsets.eventrequest;

import kaap.veiko.debuggerforker.commands.CommandBase;
import kaap.veiko.debuggerforker.commands.CommandVisitor;
import kaap.veiko.debuggerforker.commands.MutableReplyPacket;
import kaap.veiko.debuggerforker.commands.commandsets.CommandIdentifier;
import kaap.veiko.debuggerforker.commands.parser.CommandDataReader;
import kaap.veiko.debuggerforker.packet.ReplyPacket;
import kaap.veiko.debuggerforker.types.DataWriter;

public class ClearEventRequestReply extends CommandBase<ReplyPacket> {
  private static final CommandIdentifier COMMAND_IDENTIFIER = CommandIdentifier.CLEAR_EVENT_REQUEST_REPLY;

  public static ClearEventRequestReply create(int packetId) {
    MutableReplyPacket packet = MutableReplyPacket.create(packetId);

    return new ClearEventRequestReply(packet);
  }

  public static ClearEventRequestReply read(CommandDataReader reader) {
    return new ClearEventRequestReply((ReplyPacket) reader.getPacket());
  }

  private ClearEventRequestReply(ReplyPacket packet) {
    super(packet, COMMAND_IDENTIFIER);
  }

  @Override
  public void writeCommand(DataWriter writer) {
  }

  @Override
  public void visit(CommandVisitor visitor) {
    visitor.visit(this);
  }

  @Override
  public String toString() {
    return "ClearEventRequestReply{}";
  }
}
