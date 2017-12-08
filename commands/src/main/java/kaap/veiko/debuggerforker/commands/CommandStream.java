package kaap.veiko.debuggerforker.commands;

import java.io.IOException;
import java.util.Optional;

import kaap.veiko.debuggerforker.commands.parser.CommandParser;
import kaap.veiko.debuggerforker.packet.Packet;
import kaap.veiko.debuggerforker.packet.PacketStream;
import kaap.veiko.debuggerforker.types.VMInformation;

public class CommandStream implements AutoCloseable {
  private final PacketStream packetStream;
  private final VMInformation vmInformation;
  private final CommandParser commandParser;

  public CommandStream(PacketStream packetStream, VMInformation vmInformation) {
    this.packetStream = packetStream;
    this.vmInformation = vmInformation;
    this.commandParser = new CommandParser(vmInformation);
  }

  public Command read() throws IOException {
    return Optional.ofNullable(packetStream.read())
        .map(commandParser::parse)
        .orElse(null);
  }

  public void write(Command command) throws IOException {
    if (command != null && command.getPacket() != null) {
      packetStream.write(command.getPacket());
    }
  }

  @Override
  public void close() throws Exception {
    packetStream.close();
  }
}
