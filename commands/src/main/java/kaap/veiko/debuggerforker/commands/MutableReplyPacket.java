package kaap.veiko.debuggerforker.commands;

import kaap.veiko.debuggerforker.packet.ReplyPacket;

public class MutableReplyPacket extends ReplyPacket {

  private byte[] data;

  public static MutableReplyPacket create(int packetId) {
    return new MutableReplyPacket(packetId, (short) 0);
  }

  @SuppressWarnings("argument.type.incompatible") // TODO: Mutable packets shouldn't use null for PacketSource
  private MutableReplyPacket(int id, short errorCode) {
    super(id, errorCode, new byte[0], null);
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
