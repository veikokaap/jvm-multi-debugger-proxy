package kaap.veiko.debuggerforker.commands;

import java.nio.ByteBuffer;
import java.util.List;

public interface Command {
  int getCommandSetId();
  int getCommandId();
  boolean isReply();
  List<Object> getPacketValues();
}
