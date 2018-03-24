package kaap.veiko.debuggerforker.commands.commandsets;

import static org.junit.Assert.*;

import java.util.concurrent.ThreadLocalRandom;

import kaap.veiko.debuggerforker.commands.Command;
import kaap.veiko.debuggerforker.commands.parser.CommandType;
import kaap.veiko.debuggerforker.packet.CommandPacket;
import kaap.veiko.debuggerforker.packet.Packet;
import kaap.veiko.debuggerforker.packet.ReplyPacket;

public abstract class CommandTestBase extends TestBase {

  protected int generateRandomPacketId() {
    return ThreadLocalRandom.current().nextInt();
  }

  protected void assertCommandIdentifier(CommandIdentifier expectedIdentifier, Command actualCommand) {
    boolean expectedIsReply = expectedIdentifier.getType() == CommandType.REPLY;

    assertEquals(expectedIdentifier.getCommandId(), actualCommand.getCommandId());
    assertEquals(expectedIdentifier.getCommandSetId(), actualCommand.getCommandSetId());
    assertEquals(expectedIsReply, actualCommand.isReply());
  }

  protected void assertEqualPackets(Packet expected, Packet actual) {
    if (expected instanceof CommandPacket && actual instanceof CommandPacket) {
      assertEqualCommandPackets((CommandPacket) expected, (CommandPacket)actual);
    }
    else if (expected instanceof ReplyPacket && actual instanceof ReplyPacket) {
      assertEqualReplyPackets((ReplyPacket) expected, (ReplyPacket)actual);
    }
    else {
      assertTrue("Packets are different types",false);
    }
  }

  protected void assertEqualReplyPackets(ReplyPacket expected, ReplyPacket actual) {
    assertPacketsBase(expected, actual);
    assertEquals(expected.getErrorCode(), actual.getErrorCode());
    assertEquals(true, actual.isReply());
  }

  protected void assertEqualCommandPackets(CommandPacket expected, CommandPacket actual) {
    assertPacketsBase(expected, actual);
    assertEquals(expected.getCommandId(), actual.getCommandId());
    assertEquals(expected.getCommandSetId(), actual.getCommandSetId());
    assertEquals(false, actual.isReply());
  }

  private void assertPacketsBase(Packet expected, Packet actual) {
    assertEquals(expected.getId(), actual.getId());
    assertEquals(expected.getLength(), actual.getLength());
    assertEquals(expected.getFlags(), actual.getFlags());
    assertEquals(expected.isReply(), actual.isReply());
    assertArrayEquals(expected.getData(), actual.getData());
  }
}
