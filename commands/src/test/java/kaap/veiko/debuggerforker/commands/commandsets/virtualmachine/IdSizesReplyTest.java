package kaap.veiko.debuggerforker.commands.commandsets.virtualmachine;

import static kaap.veiko.debuggerforker.commands.commandsets.CommandIdentifier.ID_SIZES_REPLY;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import kaap.veiko.debuggerforker.commands.Command;
import kaap.veiko.debuggerforker.commands.commandsets.CommandIdentifier;
import kaap.veiko.debuggerforker.commands.commandsets.CommandTestBase;
import kaap.veiko.debuggerforker.commands.parser.CommandParser;
import kaap.veiko.debuggerforker.packet.CommandPacket;
import kaap.veiko.debuggerforker.packet.ReplyPacket;
import kaap.veiko.debuggerforker.types.jdwp.IdSizes;

public class IdSizesReplyTest extends CommandTestBase {

  @Test
  public void testCreate() {
    int packetId = generateRandomPacketId();
    IdSizes expectedIdSizes = new IdSizes(1, 2, 3, 4, 5);

    IdSizesReply reply = IdSizesReply.create(packetId, expectedIdSizes, getVmInformation());

    assertEquals(packetId, reply.getPacket().getId());
    assertCommandIdentifier(ID_SIZES_REPLY, reply);
    assertEquals(expectedIdSizes, reply.getIdSizes());
  }

  @Test
  public void testParse() {
    int packetId = generateRandomPacketId();
    IdSizes expectedIdSizes = new IdSizes(1, 2, 3, 4, 5);

    IdSizesReply originalCommand = IdSizesReply.create(packetId, expectedIdSizes, getVmInformation());
    ReplyPacket originalPacket = originalCommand.getPacket();
    // Needed for parsing since replies don't contain info about what type they are
    originalPacket.setCommandPacket(new CommandPacket(packetId, ID_SIZES_REPLY.getCommandSetId(), ID_SIZES_REPLY.getCommandId(), new byte[0], null));

    CommandParser parser = new CommandParser(getVmInformation());
    Command parsedCommand = parser.parse(originalPacket);

    assertEqualPackets(originalPacket, parsedCommand.getPacket());
    assertEquals(true, parsedCommand instanceof IdSizesReply);
    assertEquals(packetId, parsedCommand.getPacket().getId());
    assertEquals(expectedIdSizes, ((IdSizesReply) parsedCommand).getIdSizes());
  }
}