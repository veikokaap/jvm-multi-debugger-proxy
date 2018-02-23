package kaap.veiko.debuggerforker.commands;

import kaap.veiko.debuggerforker.commands.commandsets.CommandIdentifier;
import kaap.veiko.debuggerforker.packet.CommandPacket;

public class MutableCommandPacket extends CommandPacket {

  private byte[] data;

  public static MutableCommandPacket create(int packetId, CommandIdentifier commandIdentifier) {
    return new MutableCommandPacket(packetId, commandIdentifier.getCommandSetId(), commandIdentifier.getCommandId());
  }

  private MutableCommandPacket(int id, short commandSetId, short commandId) {
    super(id, commandSetId, commandId, new byte[0], null);
    this.data = super.getData();
  }

  public void setData(byte[] data) {
    this.data = data;
  }

  @Override
  public byte[] getData() {
    return data;
  }
}
