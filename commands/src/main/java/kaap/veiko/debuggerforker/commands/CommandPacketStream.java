package kaap.veiko.debuggerforker.commands;

import java.io.IOException;
import java.util.Optional;

import kaap.veiko.debuggerforker.commands.parser.CommandParser;
import kaap.veiko.debuggerforker.packet.PacketStream;
import kaap.veiko.debuggerforker.types.VMInformation;

public class CommandPacketStream implements AutoCloseable{
  private final PacketStream packetStream;
  private final VMInformation vmInformation;
  private final CommandParser commandParser;

  public CommandPacketStream(PacketStream packetStream, VMInformation vmInformation) {
    this.packetStream = packetStream;
    this.vmInformation = vmInformation;
    this.commandParser = new CommandParser(vmInformation);
  }

  public CommandPacket read() throws IOException {
    return Optional.ofNullable(packetStream.read())
        .map(p -> new CommandPacket(p, commandParser.parse(p), packetStream.getPacketSource()))
        .orElse(null);
  }

  public void write(CommandPacket packet) throws IOException {
    packetStream.write(packet);
  }

  @Override
  public void close() throws Exception {
    packetStream.close();
  }
}
