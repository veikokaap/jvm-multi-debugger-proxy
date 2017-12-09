package kaap.veiko.debuggerforker.commands.sets.virtualmachine;

import kaap.veiko.debuggerforker.commands.CommandBase;
import kaap.veiko.debuggerforker.commands.CommandVisitor;
import kaap.veiko.debuggerforker.commands.PacketBuilder;
import kaap.veiko.debuggerforker.commands.parser.CommandDataWriter;
import kaap.veiko.debuggerforker.commands.sets.CommandIdentifier;
import kaap.veiko.debuggerforker.packet.Packet;

public class DisposeReply extends CommandBase {
  public static final CommandIdentifier COMMAND_IDENTIFIER = CommandIdentifier.DISPOSE_REPLY;

  public DisposeReply(Packet packet) {
    super();
    setPacket(packet);
  }

  public DisposeReply(PacketBuilder packetBuilder) {
    super();
    createSyntheticPacket(packetBuilder);
  }

  @Override
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
