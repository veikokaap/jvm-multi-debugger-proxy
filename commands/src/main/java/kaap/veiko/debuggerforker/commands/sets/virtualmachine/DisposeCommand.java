package kaap.veiko.debuggerforker.commands.sets.virtualmachine;

import kaap.veiko.debuggerforker.commands.CommandBase;
import kaap.veiko.debuggerforker.commands.CommandVisitor;
import kaap.veiko.debuggerforker.commands.MutableCommandPacket;
import kaap.veiko.debuggerforker.commands.parser.CommandDataReader;
import kaap.veiko.debuggerforker.commands.parser.CommandDataWriter;
import kaap.veiko.debuggerforker.commands.sets.CommandIdentifier;
import kaap.veiko.debuggerforker.packet.Packet;

public class DisposeCommand extends CommandBase {
  private static final CommandIdentifier COMMAND_IDENTIFIER = CommandIdentifier.DISPOSE_COMMAND;

  public static DisposeCommand create(int packetId) {
    MutableCommandPacket packet = MutableCommandPacket.create(packetId, COMMAND_IDENTIFIER);
    return new DisposeCommand(packet);
  }

  public static DisposeCommand read(CommandDataReader reader) {
    return new DisposeCommand(reader.getPacket());
  }

  private DisposeCommand(Packet packet) {
    super(packet, COMMAND_IDENTIFIER);
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
    return "DisposeCommand{}";
  }
}
