package kaap.veiko.debuggerforker.commands;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.commands.parser.CommandDataWriter;
import kaap.veiko.debuggerforker.commands.parser.CommandType;
import kaap.veiko.debuggerforker.commands.sets.CommandIdentifier;
import kaap.veiko.debuggerforker.packet.Packet;
import kaap.veiko.debuggerforker.packet.PacketImpl;
import kaap.veiko.debuggerforker.types.VMInformation;

public abstract class CommandBase implements Command {

  private final Packet packet;

  protected CommandBase(Packet packet) {
    this.packet = packet;
  }

  @Override
  public Packet getPacket() {
    return packet;
  }

  @Override
  public int getCommandSetId() {
    return getCommandIdentifier().getCommandSetId();
  }

  @Override
  public int getCommandId() {
    return getCommandIdentifier().getCommandId();
  }

  @Override
  public boolean isReply() {
    return getCommandIdentifier().getType() == CommandType.REPLY;
  }

  protected abstract CommandIdentifier getCommandIdentifier();

  @Override
  public Packet asPacket(int id, VMInformation vmInformation) {
    Packet packet = new PacketImpl();

    packet.setId(id);
    packet.setCommandSetId((short) getCommandSetId());
    packet.setCommandId((short) getCommandId());

    if (isReply()) {
      packet.setFlags((short) -128);
    }

    packet.setDataBytes(asBytes(vmInformation));
    packet.setLength(Packet.HEADER_LENGTH + packet.getDataBytes().length);

    return packet;
  }

  private byte[] asBytes(VMInformation vmInformation) {
    ByteBuffer byteBuffer = ByteBuffer.allocate(4096);
    CommandDataWriter writer = new CommandDataWriter(byteBuffer, vmInformation);
    this.writeCommand(writer);

    byte[] dataBytes = new byte[byteBuffer.remaining()];
    byteBuffer.get(dataBytes);

    return dataBytes;
  }

  protected abstract void writeCommand(CommandDataWriter writer);
}
