package kaap.veiko.debuggerforker.commands;

import kaap.veiko.debuggerforker.packet.ReplyPacket;

public class MutableReplyPacket extends ReplyPacket {

  private byte[] data;

  public static MutableReplyPacket create(int packetId) {
    return new MutableReplyPacket(packetId, (short) 0);
  }

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
