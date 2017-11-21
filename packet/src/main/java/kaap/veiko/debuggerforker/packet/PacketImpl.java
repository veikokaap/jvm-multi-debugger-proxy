package kaap.veiko.debuggerforker.packet;

import java.util.Arrays;

public class PacketImpl implements Packet {

  private int length = 0;
  private int id = 0;
  private short flags = 0;
  private short commandSetId = 0;
  private short commandId = 0;
  private short errorCode = 0;
  private byte[] data = new byte[]{};

  public PacketImpl() {
  }

  @Override
  public int getLength() {
    return length;
  }

  @Override
  public void setLength(int length) {
    this.length = length;
  }

  @Override
  public int getId() {
    return id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  @Override
  public short getFlags() {
    return flags;
  }

  @Override
  public void setFlags(short flags) {
    this.flags = flags;
  }

  @Override
  public short getCommandSetId() {
    return commandSetId;
  }

  @Override
  public void setCommandSetId(short commandSetId) {
    this.commandSetId = commandSetId;
  }

  @Override
  public short getCommandId() {
    return commandId;
  }

  @Override
  public void setCommandId(short commandId) {
    this.commandId = commandId;
  }

  @Override
  public short getErrorCode() {
    return errorCode;
  }

  @Override
  public void setErrorCode(short errorCode) {
    this.errorCode = errorCode;
  }

  @Override
  public byte[] getDataBytes() {
    return data;
  }

  @Override
  public void setDataBytes(byte[] dataBytes) {
    this.data = dataBytes;
  }

  @Override
  public boolean isReply() {
    return getFlags() == -128;
  }

  @Override
  public boolean hasData() {
    return getLength() > HEADER_LENGTH;
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();

    if (isReply()) {
      stringBuilder.append("Reply {")
          .append("id=").append(id)
          .append(", commandSetId=").append(commandSetId)
          .append(", commandId=").append(commandId)
          .append(", errorCode=").append(errorCode);
    }
    else {
      stringBuilder.append("Packet{")
          .append("id=").append(id)
          .append(", commandSetId=").append(commandSetId)
          .append(", commandId=").append(commandId);
    }

    if (hasData()) {
      stringBuilder.append(", data=").append(Arrays.toString(data));
    }

    return stringBuilder.append("}").toString();
  }
}
