package kaap.veiko.debuggerforker.commands.sets.virtualmachine;

import kaap.veiko.debuggerforker.commands.CommandBase;
import kaap.veiko.debuggerforker.commands.CommandVisitor;
import kaap.veiko.debuggerforker.commands.SyntheticPacket;
import kaap.veiko.debuggerforker.commands.parser.CommandDataReader;
import kaap.veiko.debuggerforker.commands.parser.CommandDataWriter;
import kaap.veiko.debuggerforker.commands.sets.CommandIdentifier;
import kaap.veiko.debuggerforker.packet.Packet;

public class DisposeReply extends CommandBase {
  private static final CommandIdentifier COMMAND_IDENTIFIER = CommandIdentifier.DISPOSE_REPLY;

  public static DisposeReply create(int packetId) {
    SyntheticPacket packet = SyntheticPacket.create(packetId, COMMAND_IDENTIFIER);
    return new DisposeReply(packet);
  }

  public static DisposeReply read(CommandDataReader reader) {
    return new DisposeReply(reader.getPacket());
  }

  private DisposeReply(Packet packet) {
    super(packet, COMMAND_IDENTIFIER);
  }

  protected CommandIdentifier getCommandIdentifier() {
    return COMMAND_IDENTIFIER;
  }

  @Override
  public void writeCommand(CommandDataWriter writer) {
  }

  @Override
  public <T> T visit(CommandVisitor<T> visitor) {
    return visitor.visit(this);
  }

  @Override
  public String toString() {
    return "DisposeReply{}";
  }
}
