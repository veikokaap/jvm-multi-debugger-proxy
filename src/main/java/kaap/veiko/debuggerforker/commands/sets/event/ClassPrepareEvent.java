package kaap.veiko.debuggerforker.commands.sets.event;

import kaap.veiko.debuggerforker.commands.constants.EventKind;
import kaap.veiko.debuggerforker.commands.parser.annotations.JDWPCommandConstructor;
import kaap.veiko.debuggerforker.commands.parser.annotations.JDWPSubCommand;
import kaap.veiko.debuggerforker.commands.types.ReferenceTypeID;
import kaap.veiko.debuggerforker.commands.types.ThreadID;

@JDWPSubCommand(eventKind = EventKind.CLASS_PREPARE)
public class ClassPrepareEvent extends VirtualMachineEvent {
  private final int requestId;
  private final long thread;
  private final byte refTypeTag;
  private final long typeId;
  private final String signature;
  private final int status;

  @JDWPCommandConstructor
  public ClassPrepareEvent(
      int requestId,
      ThreadID thread,
      byte refTypeTag,
      ReferenceTypeID typeID,
      String signature,
      int status
  ) {
    this.requestId = requestId;
    this.thread = thread.asLong();
    this.refTypeTag = refTypeTag;
    this.typeId = typeID.asLong();
    this.signature = signature;
    this.status = status;
  }

  public int getRequestId() {
    return requestId;
  }

  public long getThread() {
    return thread;
  }

  public byte getRefTypeTag() {
    return refTypeTag;
  }

  public long getTypeId() {
    return typeId;
  }

  public String getSignature() {
    return signature;
  }

  public int getStatus() {
    return status;
  }

  @Override
  public String toString() {
    return "ClassPrepareEvent{" +
        "requestId=" + requestId +
        ", thread=" + thread +
        ", refTypeTag=" + refTypeTag +
        ", typeId=" + typeId +
        ", signature='" + signature + '\'' +
        ", status=" + status +
        '}';
  }
}
