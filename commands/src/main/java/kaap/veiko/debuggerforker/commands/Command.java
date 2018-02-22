package kaap.veiko.debuggerforker.commands;

import kaap.veiko.debuggerforker.commands.parser.CommandDataWriter;
import kaap.veiko.debuggerforker.packet.Packet;
import kaap.veiko.debuggerforker.packet.PacketSource;

public interface Command {
  int getCommandSetId();

  int getCommandId();

  boolean isReply();

  <T> T visit(CommandVisitor<T> visitor);

  Packet getPacket();

  PacketSource getSource();

  void writeCommand(CommandDataWriter writer);
}
