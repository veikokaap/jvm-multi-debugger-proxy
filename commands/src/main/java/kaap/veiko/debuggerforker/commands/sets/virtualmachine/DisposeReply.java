package kaap.veiko.debuggerforker.commands.sets.virtualmachine;

import kaap.veiko.debuggerforker.commands.CommandBase;
import kaap.veiko.debuggerforker.commands.CommandVisitor;
import kaap.veiko.debuggerforker.commands.MutableReplyPacket;
import kaap.veiko.debuggerforker.commands.parser.CommandDataReader;
import kaap.veiko.debuggerforker.commands.parser.CommandDataWriter;
import kaap.veiko.debuggerforker.commands.sets.CommandIdentifier;
import kaap.veiko.debuggerforker.packet.ReplyPacket;

public class DisposeReply extends CommandBase<ReplyPacket> {
  private static final CommandIdentifier COMMAND_IDENTIFIER = CommandIdentifier.DISPOSE_REPLY;

  public static DisposeReply create(int packetId) {
    MutableReplyPacket packet = MutableReplyPacket.create(packetId);
    return new DisposeReply(packet);
  }

  public static DisposeReply read(CommandDataReader reader) {
    return new DisposeReply((ReplyPacket) reader.getPacket());
  }

  private DisposeReply(ReplyPacket packet) {
    super(packet, COMMAND_IDENTIFIER);
  }

  protected CommandIdentifier getCommandIdentifier() {
    return COMMAND_IDENTIFIER;
  }

  @Override
  public void writeCommand(CommandDataWriter writer) {
  }

  @Override
  public void visit(CommandVisitor visitor) {
    visitor.visit(this);
  }

  @Override
  public String toString() {
    return "DisposeReply{}";
  }
}
