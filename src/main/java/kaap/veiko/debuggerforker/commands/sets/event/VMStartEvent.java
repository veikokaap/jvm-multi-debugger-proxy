package kaap.veiko.debuggerforker.commands.sets.event;

import kaap.veiko.debuggerforker.commands.constants.EventKindConstants;
import kaap.veiko.debuggerforker.commands.parser.annotations.JDWPCommandConstructor;
import kaap.veiko.debuggerforker.commands.parser.annotations.JDWPSubCommand;
import kaap.veiko.debuggerforker.commands.types.ThreadID;


@JDWPSubCommand(id = EventKindConstants.VM_START)
public class VMStartEvent extends VirtualMachineEvent {
    private final int requestId;
    private final long thread;

    @JDWPCommandConstructor
    public VMStartEvent(
            int requestId,
            ThreadID thread
    ) {
        this.requestId = requestId;
        this.thread = thread.asLong();
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
