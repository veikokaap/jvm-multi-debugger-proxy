package kaap.veiko.debuggerforker.packet;

import java.util.Arrays;

public class Packet {

  public static final int HEADER_LENGTH = 11;

  private int length = 0;
  private int id = 0;
  private short flags = 0;
  private short commandSet = 0;
  private short command = 0;
  private short errorCode = 0;
  private byte[] data = new byte[]{};

  public Packet() {
  }

  public int getLength() {
    return length;
  }

  public void setLength(int length) {
    this.length = length;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public short getFlags() {
    return flags;
  }

  public void setFlags(short flags) {
    this.flags = flags;
  }

  public short getCommandSet() {
    return commandSet;
  }

  public void setCommandSet(short commandSet) {
    this.commandSet = commandSet;
  }

  public short getCommand() {
    return command;
  }

  public void setCommand(short command) {
    this.command = command;
  }

  public short getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(short errorCode) {
    this.errorCode = errorCode;
  }

  public byte[] getDataBytes() {
    return data;
  }

  public void setDataBytes(byte[] dataBytes) {
    this.data = dataBytes;
  }

  public boolean isReply() {
    return getFlags() == -128;
  }

  public boolean hasData() {
    return getLength() > HEADER_LENGTH;
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();

    if (isReply()) {
      stringBuilder.append("Reply {")
          .append("id=").append(id)
          .append(", commandSet=").append(commandSet)
          .append(", command=").append(command)
          .append(", errorCode=").append(errorCode);
    }
    else {
      stringBuilder.append("Packet{")
          .append("id=").append(id)
          .append(", commandSet=").append(commandSet)
          .append(", command=").append(command);
    }

    if (hasData()) {
      stringBuilder.append(", data=").append(Arrays.toString(data));
    }

    return stringBuilder.append("}").toString();
  }
}
