package kaap.veiko.debuggerforker.commands;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;

import kaap.veiko.debuggerforker.commands.parser.CommandDataWriter;
import kaap.veiko.debuggerforker.packet.Packet;
import kaap.veiko.debuggerforker.types.VMInformation;

public class PacketBuilder {

  private final VMInformation vmInformation;
  private final AtomicInteger idCounter = new AtomicInteger(-1);

  public PacketBuilder(VMInformation vmInformation) {
    this.vmInformation = vmInformation;
  }

  public Packet build(Command command) {
    byte[] data = readDataBytes(command);
    int length = Packet.HEADER_LENGTH + data.length;
    short flags = (short) (command.isReply() ? -128 : 0);

    Packet packet = new SyntheticPacket(
        length, idCounter.getAndDecrement(), flags, (short) 0, (short) command.getCommandSetId(), (short) command.getCommandId(), data
    );

    return packet;
  }

  private byte[] readDataBytes(Command command) {
    ByteBuffer byteBuffer = ByteBuffer.allocate(4096);
    CommandDataWriter writer = new CommandDataWriter(byteBuffer, vmInformation);
    command.writeCommand(writer);

    byte[] dataBytes = new byte[byteBuffer.remaining()];
    byteBuffer.get(dataBytes);

    return dataBytes;
  }
}
