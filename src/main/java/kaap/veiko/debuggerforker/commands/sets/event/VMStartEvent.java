package kaap.veiko.debuggerforker.commands.sets.event;

import kaap.veiko.debuggerforker.commands.constants.EventKindConstants;
import kaap.veiko.debuggerforker.commands.parser.JDWPCommandContent;
import kaap.veiko.debuggerforker.commands.parser.JDWPDataType;

import static kaap.veiko.debuggerforker.commands.constants.DataType.THREAD_ID;

@JDWPCommandContent(id = EventKindConstants.VM_START)
public class VMStartEvent extends VirtualMachineEvent {
    private final int requestId;
    private final long thread;

    public VMStartEvent(
            int requestId,
            @JDWPDataType(THREAD_ID) long thread
    ) {
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
