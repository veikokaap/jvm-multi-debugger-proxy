package kaap.veiko.debuggerforker.events;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.commands.constants.EventKind;
import kaap.veiko.debuggerforker.commands.parser.annotations.JdwpCommandConstructor;
import kaap.veiko.debuggerforker.types.ReferenceTypeId;
import kaap.veiko.debuggerforker.types.ThreadId;
import kaap.veiko.debuggerforker.utils.ByteBufferUtil;

@JdwpEvent(EventKind.CLASS_PREPARE)
public class ClassPrepareEvent extends VirtualMachineEvent {
  private final int requestId;
  private final ThreadId thread;
  private final byte refTypeTag;
  private final ReferenceTypeId typeId;
  private final String signature;
  private final int status;

  @JdwpCommandConstructor
  public ClassPrepareEvent(
      int requestId,
      ThreadId thread,
      byte refTypeTag,
      ReferenceTypeId typeId,
      String signature,
      int status
  ) {
    this.requestId = requestId;
    this.thread = thread;
    this.refTypeTag = refTypeTag;
    this.typeId = typeId;
    this.signature = signature;
    this.status = status;
  }

  public int getRequestId() {
    return requestId;
  }

  public ThreadId getThread() {
    return thread;
  }

  public byte getRefTypeTag() {
    return refTypeTag;
  }

  public ReferenceTypeId getTypeId() {
    return typeId;
  }

  public String getSignature() {
    return signature;
  }

  public int getStatus() {
    return status;
  }

  @Override
  public void putToBuffer(ByteBuffer buffer) {
    buffer.putInt(requestId);
    thread.putToBuffer(buffer);
    buffer.put(refTypeTag);
    typeId.putToBuffer(buffer);
    ByteBufferUtil.putString(buffer, signature);
    buffer.putInt(status);
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
