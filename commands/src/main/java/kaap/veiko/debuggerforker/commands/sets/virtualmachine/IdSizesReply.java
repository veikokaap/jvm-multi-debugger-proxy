package kaap.veiko.debuggerforker.commands.sets.virtualmachine;

import kaap.veiko.debuggerforker.commands.CommandBase;
import kaap.veiko.debuggerforker.commands.CommandVisitor;
import kaap.veiko.debuggerforker.commands.SyntheticPacket;
import kaap.veiko.debuggerforker.commands.parser.CommandDataReader;
import kaap.veiko.debuggerforker.commands.parser.CommandDataWriter;
import kaap.veiko.debuggerforker.commands.sets.CommandIdentifier;
import kaap.veiko.debuggerforker.commands.util.CommandDataUtil;
import kaap.veiko.debuggerforker.packet.Packet;
import kaap.veiko.debuggerforker.types.VMInformation;
import kaap.veiko.debuggerforker.types.jdwp.IdSizes;

public class IdSizesReply extends CommandBase {
  private static final CommandIdentifier COMMAND_IDENTIFIER = CommandIdentifier.ID_SIZES_REPLY;

  private final IdSizes idSizes;

  public static IdSizesReply create(int packetId, VMInformation vmInformation, IdSizes idSizes) {
    SyntheticPacket packet = SyntheticPacket.create(packetId, COMMAND_IDENTIFIER);
    IdSizesReply command = new IdSizesReply(packet, idSizes);
    packet.setDataBytes(CommandDataUtil.getCommandDataBytes(command, vmInformation));

    return command;
  }

  public static IdSizesReply read(CommandDataReader reader) {
    IdSizes idSizes = IdSizes.read(reader);
    return new IdSizesReply(reader.getPacket(), idSizes);
  }

  private IdSizesReply(Packet packet, IdSizes idSizes) {
    super(packet, COMMAND_IDENTIFIER);
    this.idSizes = idSizes;
  }

  @Override
  public void writeCommand(CommandDataWriter writer) {
    writer.writeType(idSizes);
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
    return "IdSizesReply{" +
        "idSizes=" + idSizes +
        '}';
  }
}
