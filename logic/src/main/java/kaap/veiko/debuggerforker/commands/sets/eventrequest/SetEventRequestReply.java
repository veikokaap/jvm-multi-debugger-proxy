package kaap.veiko.debuggerforker.commands.sets.eventrequest;


import java.util.Collections;
import java.util.List;

import kaap.veiko.debuggerforker.commands.CommandBase;
import kaap.veiko.debuggerforker.commands.parser.annotations.JdwpCommand;
import kaap.veiko.debuggerforker.commands.parser.annotations.JdwpCommandConstructor;
import kaap.veiko.debuggerforker.commands.sets.CommandIdentifier;

@JdwpCommand(CommandIdentifier.SET_EVENT_REQUEST_REPLY)
public class SetEventRequestReply extends CommandBase {
  private final int requestId;

  @JdwpCommandConstructor
  public SetEventRequestReply(int requestId) {
    this.requestId = requestId;
  }

  public int getRequestId() {
    return requestId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    SetEventRequestReply that = (SetEventRequestReply) o;

    return requestId == that.requestId;
  }

  @Override
  public List<Object> getPacketValues() {
    return Collections.singletonList(requestId);
  }

  @Override
  public int hashCode() {
    return requestId;
  }

  @Override
  public String toString() {
    return "SetEventRequestReply{" +
        "requestId=" + requestId +
        '}';
  }
}
