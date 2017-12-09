package kaap.veiko.debuggerforker.commands;

import kaap.veiko.debuggerforker.commands.parser.CommandDataWriter;
import kaap.veiko.debuggerforker.packet.Packet;

public interface Command {
  int getCommandSetId();

  int getCommandId();

  boolean isReply();

  <T> T visit(CommandVisitor<T> visitor);

  Packet getPacket();

  void writeCommand(CommandDataWriter writer);
}
