package kaap.veiko.debuggerforker.packet;

public interface Packet {
  int HEADER_LENGTH = 11;

  int getLength();

  void setLength(int length);

  int getId();

  void setId(int id);

  short getFlags();

  void setFlags(short flags);

  short getCommandSetId();

  void setCommandSetId(short commandSetId);

  short getCommandId();

  void setCommandId(short commandId);

  short getErrorCode();

  void setErrorCode(short errorCode);

  byte[] getDataBytes();

  void setDataBytes(byte[] dataBytes);

  boolean isReply();

  boolean hasData();

  PacketStream getSource();

  void setSource(PacketStream source);
}
