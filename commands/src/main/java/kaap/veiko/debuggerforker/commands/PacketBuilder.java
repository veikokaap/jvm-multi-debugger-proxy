package kaap.veiko.debuggerforker.commands;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.commands.parser.CommandDataWriter;
import kaap.veiko.debuggerforker.packet.Packet;
import kaap.veiko.debuggerforker.packet.PacketImpl;
import kaap.veiko.debuggerforker.types.VMInformation;

public class PacketBuilder {

  private final VMInformation vmInformation;

  public PacketBuilder(VMInformation vmInformation) {
    this.vmInformation = vmInformation;
  }

  public Packet build(Command command, int id) {
    Packet packet = new PacketImpl();

    packet.setId(id);
    packet.setCommandSetId((short) command.getCommandSetId());
    packet.setCommandId((short) command.getCommandId());

    if (command.isReply()) {
      packet.setFlags((short) -128);
    }

    packet.setDataBytes(asBytes(command));
    packet.setLength(Packet.HEADER_LENGTH + packet.getDataBytes().length);

    return packet;
  }

  private byte[] asBytes(Command command) {
    ByteBuffer byteBuffer = ByteBuffer.allocate(4096);
    CommandDataWriter writer = new CommandDataWriter(byteBuffer, vmInformation);
    command.writeCommand(writer);

    byte[] dataBytes = new byte[byteBuffer.remaining()];
    byteBuffer.get(dataBytes);

    return dataBytes;
  }
}
