package kaap.veiko.debuggerforker.commands;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaap.veiko.debuggerforker.commands.parser.CommandParser;
import kaap.veiko.debuggerforker.packet.PacketSource;
import kaap.veiko.debuggerforker.packet.PacketStream;
import kaap.veiko.debuggerforker.types.VMInformation;

public class CommandStream {

  private static final Logger log = LoggerFactory.getLogger(CommandStream.class);

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

  public SocketChannel getSocketChannel() {
    return packetStream.getSocketChannel();
  }

  public PacketSource getSource() {
    return packetStream.getSource();
  }

  public void close() {
    packetStream.close();
  }

  public boolean isClosed() {
    return packetStream.isClosed();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    CommandStream that = (CommandStream) o;

    return packetStream.equals(that.packetStream);
  }

  @Override
  public int hashCode() {
    return packetStream.hashCode();
  }

  @Override
  public String toString() {
    return "CommandStream{" +
        "packetStream=" + packetStream +
        '}';
  }
}
