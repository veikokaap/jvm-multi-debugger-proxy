package kaap.veiko.debuggerforker.commands.commandsets.virtualmachine;

import static org.junit.Assert.*;

import org.junit.Test;

import kaap.veiko.debuggerforker.commands.Command;
import kaap.veiko.debuggerforker.commands.commandsets.CommandIdentifier;
import kaap.veiko.debuggerforker.commands.commandsets.CommandTestBase;
import kaap.veiko.debuggerforker.commands.parser.CommandParser;
import kaap.veiko.debuggerforker.packet.Packet;

public class DisposeCommandTest extends CommandTestBase {

  @Test
  public void testCreate() {
    int packetId = generateRandomPacketId();

    DisposeCommand command = DisposeCommand.create(packetId);

    assertEquals(packetId, command.getPacket().getId());
    assertCommandIdentifier(CommandIdentifier.DISPOSE_COMMAND, command);
  }

  @Test
  public void testParse() {
    int packetId = generateRandomPacketId();
    DisposeCommand originalCommand = DisposeCommand.create(packetId);
    Packet originalPacket = originalCommand.getPacket();

    CommandParser parser = new CommandParser(getVmInformation());
    Command parsedCommand = parser.parse(originalPacket);

    assertEqualPackets(originalPacket, parsedCommand.getPacket());
    assertEquals(true, parsedCommand instanceof DisposeCommand);
    assertEquals(packetId, parsedCommand.getPacket().getId());
  }
}