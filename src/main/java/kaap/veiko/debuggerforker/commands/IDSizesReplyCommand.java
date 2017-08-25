package kaap.veiko.debuggerforker.commands;

import kaap.veiko.debuggerforker.commands.parser.CommandType;
import kaap.veiko.debuggerforker.commands.parser.JDWPCommand;

@JDWPCommand(commandSet = 1, command = 7, commandType = CommandType.REPLY)
public class IDSizesReplyCommand implements Command {

    private final int fieldIDSize;
    private final int methodIDSize;
    private final int objectIDSize;
    private final int referenceTypeIDSize;
    private final int frameIDSize;

    public IDSizesReplyCommand(int fieldIDSize, int methodIDSize, int objectIDSize, int referenceTypeIDSize, int frameIDSize) {
        this.fieldIDSize = fieldIDSize;
        this.methodIDSize = methodIDSize;
        this.objectIDSize = objectIDSize;
        this.referenceTypeIDSize = referenceTypeIDSize;
        this.frameIDSize = frameIDSize;
    }

    public int getFieldIDSize() {
        return fieldIDSize;
    }

    public int getMethodIDSize() {
        return methodIDSize;
    }

    public int getObjectIDSize() {
        return objectIDSize;
    }

    public int getReferenceTypeIDSize() {
        return referenceTypeIDSize;
    }

    public int getFrameIDSize() {
        return frameIDSize;
    }

    @Override
    public String toString() {
        return "IDSizesReplyCommand{" +
                "fieldIDSize=" + fieldIDSize +
                ", methodIDSize=" + methodIDSize +
                ", objectIDSize=" + objectIDSize +
                ", referenceTypeIDSize=" + referenceTypeIDSize +
                ", frameIDSize=" + frameIDSize +
                '}';
    }
}
