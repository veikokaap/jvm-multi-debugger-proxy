package kaap.veiko.debuggerforker.commands;

import kaap.veiko.debuggerforker.packet.Packet;
import kaap.veiko.debuggerforker.packet.PacketSource;
import kaap.veiko.debuggerforker.types.DataWriter;

public interface Command<T extends Packet> {
  int getCommandSetId();

  int getCommandId();

  boolean isReply();

  void visit(CommandVisitor visitor);

  T getPacket();

  PacketSource getSource();

  void writeCommand(DataWriter writer);
}
