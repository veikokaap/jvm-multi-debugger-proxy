package kaap.veiko.debuggerforker.commands.commandsets.virtualmachine;

import kaap.veiko.debuggerforker.commands.CommandBase;
import kaap.veiko.debuggerforker.commands.CommandVisitor;
import kaap.veiko.debuggerforker.commands.MutableReplyPacket;
import kaap.veiko.debuggerforker.commands.parser.CommandDataReader;
import kaap.veiko.debuggerforker.commands.commandsets.CommandIdentifier;
import kaap.veiko.debuggerforker.commands.util.CommandDataUtil;
import kaap.veiko.debuggerforker.packet.ReplyPacket;
import kaap.veiko.debuggerforker.types.DataWriter;
import kaap.veiko.debuggerforker.types.VMInformation;
import kaap.veiko.debuggerforker.types.jdwp.IdSizes;

public class IdSizesReply extends CommandBase<ReplyPacket> {
  private static final CommandIdentifier COMMAND_IDENTIFIER = CommandIdentifier.ID_SIZES_REPLY;

  private final IdSizes idSizes;

  public static IdSizesReply create(int packetId, VMInformation vmInformation, IdSizes idSizes) {
    MutableReplyPacket packet = MutableReplyPacket.create(packetId);
    IdSizesReply command = new IdSizesReply(packet, idSizes);
    packet.setData(CommandDataUtil.getCommandDataBytes(command, vmInformation));

    return command;
  }

  public static IdSizesReply read(CommandDataReader reader) {
    IdSizes idSizes = IdSizes.read(reader);
    return new IdSizesReply((ReplyPacket) reader.getPacket(), idSizes);
  }

  private IdSizesReply(ReplyPacket packet, IdSizes idSizes) {
    super(packet, COMMAND_IDENTIFIER);
    this.idSizes = idSizes;
  }

  @Override
  public void writeCommand(DataWriter writer) {
    writer.writeType(idSizes);
  }

  @Override
  public void visit(CommandVisitor visitor) {
    visitor.visit(this);
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
