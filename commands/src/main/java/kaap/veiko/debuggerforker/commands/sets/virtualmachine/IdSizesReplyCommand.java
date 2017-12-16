package kaap.veiko.debuggerforker.commands.sets.virtualmachine;

import kaap.veiko.debuggerforker.commands.CommandBase;
import kaap.veiko.debuggerforker.commands.CommandVisitor;
import kaap.veiko.debuggerforker.commands.parser.CommandDataWriter;
import kaap.veiko.debuggerforker.commands.sets.CommandIdentifier;
import kaap.veiko.debuggerforker.packet.Packet;
import kaap.veiko.debuggerforker.types.DataReader;
import kaap.veiko.debuggerforker.types.jdwp.IdSizes;

public class IdSizesReplyCommand extends CommandBase {
  public static final CommandIdentifier COMMAND_IDENTIFIER = CommandIdentifier.ID_SIZES_REPLY;

  private final IdSizes idSizes;

  public IdSizesReplyCommand(DataReader reader, Packet packet) {
    super();
    this.idSizes = IdSizes.read(reader);
    setPacket(packet);
  }

  @Override
  public void writeCommand(CommandDataWriter writer) {
    writer.writeType(idSizes);
  }

  @Override
  protected CommandIdentifier getCommandIdentifier() {
    return COMMAND_IDENTIFIER;
  }

  @Override
  public <T> T visit(CommandVisitor<T> visitor) {
    return visitor.visit(this);
  }

  public IdSizes getIdSizes() {
    return idSizes;
  }

  @Override
  public String toString() {
    return "IdSizesReplyCommand{" +
        "idSizes=" + idSizes +
        '}';
  }
}
