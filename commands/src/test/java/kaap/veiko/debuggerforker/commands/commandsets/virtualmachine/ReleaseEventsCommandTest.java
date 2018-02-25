package kaap.veiko.debuggerforker.commands.commandsets.virtualmachine;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import kaap.veiko.debuggerforker.commands.Command;
import kaap.veiko.debuggerforker.commands.commandsets.CommandIdentifier;
import kaap.veiko.debuggerforker.commands.commandsets.CommandTestBase;
import kaap.veiko.debuggerforker.commands.parser.CommandParser;
import kaap.veiko.debuggerforker.packet.Packet;

public class ReleaseEventsCommandTest extends CommandTestBase {

  @Test
  public void testCreate() {
    int packetId = generateRandomPacketId();

    ReleaseEventsCommand command = ReleaseEventsCommand.create(packetId);

    assertEquals(packetId, command.getPacket().getId());
    assertCommandIdentifier(CommandIdentifier.RELEASE_EVENTS_COMMAND, command);
  }

  @Test
  public void testParse() {
    int packetId = generateRandomPacketId();
    ReleaseEventsCommand originalCommand = ReleaseEventsCommand.create(packetId);
    Packet originalPacket = originalCommand.getPacket();

    CommandParser parser = new CommandParser(getVmInformation());
    Command parsedCommand = parser.parse(originalPacket);

    assertEqualPackets(originalPacket, parsedCommand.getPacket());
    assertEquals(true, parsedCommand instanceof ReleaseEventsCommand);
    assertEquals(packetId, parsedCommand.getPacket().getId());
  }
}