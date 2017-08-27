package kaap.veiko.debuggerforker.commands.sets.event;

import kaap.veiko.debuggerforker.commands.constants.EventKindConstants;
import kaap.veiko.debuggerforker.commands.parser.JDWPCommandContent;

@JDWPCommandContent(id = EventKindConstants.VM_START)
public class VMStartEvent extends VirtualMachineEvent {
    private final int requestId;
    private final long thread;

    public VMStartEvent(int requestId, long thread) {
        this.requestId = requestId;
        this.thread = thread;
    }

    public int getRequestId() {
        return requestId;
    }

    public long getThread() {
        return thread;
    }

    @Override
    public String toString() {
        return "VMStartEvent{" +
                "requestId=" + requestId +
                ", thread=" + thread +
                '}';
    }
}
