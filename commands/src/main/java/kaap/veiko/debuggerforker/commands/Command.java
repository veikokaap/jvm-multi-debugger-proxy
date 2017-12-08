package kaap.veiko.debuggerforker.commands;

import kaap.veiko.debuggerforker.packet.Packet;
import kaap.veiko.debuggerforker.types.VMInformation;

public interface Command {
  int getCommandSetId();

  int getCommandId();

  boolean isReply();

  Packet asPacket(int id, VMInformation vmInformation);

  <T> T visit(CommandVisitor<T> visitor);

  Packet getPacket();
}
