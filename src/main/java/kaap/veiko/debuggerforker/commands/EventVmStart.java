package kaap.veiko.debuggerforker.commands;

import kaap.veiko.debuggerforker.commands.parser.JDWPCommandContent;

@JDWPCommandContent(eventKind = 90)
public class EventVmStart extends VirtualMachineEvent {
    private final int requestId;
    private final long thread;

    public EventVmStart(int requestId, long thread) {
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
        return "EventVmStart{" +
                "requestId=" + requestId +
                ", thread=" + thread +
                '}';
    }
}
