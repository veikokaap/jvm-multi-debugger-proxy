package kaap.veiko.debuggerforker.commands.commandsets.virtualmachine;

import static kaap.veiko.debuggerforker.commands.commandsets.CommandIdentifier.DISPOSE_REPLY;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import kaap.veiko.debuggerforker.commands.Command;
import kaap.veiko.debuggerforker.commands.commandsets.CommandIdentifier;
import kaap.veiko.debuggerforker.commands.commandsets.CommandTestBase;
import kaap.veiko.debuggerforker.commands.parser.CommandParser;
import kaap.veiko.debuggerforker.packet.CommandPacket;
import kaap.veiko.debuggerforker.packet.ReplyPacket;

public class DisposeReplyTest extends CommandTestBase {

  @Test
  public void testCreate() {
    int packetId = generateRandomPacketId();

    DisposeReply reply = DisposeReply.create(packetId);

    assertEquals(packetId, reply.getPacket().getId());
    assertCommandIdentifier(DISPOSE_REPLY, reply);
  }

  @Test
  @SuppressWarnings("argument.type.incompatible") // TODO: Don't use null for PacketSource
  public void testParse() {
    int packetId = generateRandomPacketId();
    DisposeReply originalReply = DisposeReply.create(packetId);
    ReplyPacket originalPacket = originalReply.getPacket();
    // Needed for parsing since replies don't contain info about what type they are
    originalPacket.setCommandPacket(new CommandPacket(packetId, DISPOSE_REPLY.getCommandSetId(), DISPOSE_REPLY.getCommandId(), new byte[0], null));

    CommandParser parser = new CommandParser(getVmInformation());
    Command parsedCommand = parser.parse(originalPacket);

    assertEqualPackets(originalPacket, parsedCommand.getPacket());
    assertEquals(true, parsedCommand instanceof DisposeReply);
    assertEquals(packetId, parsedCommand.getPacket().getId());
  }
}