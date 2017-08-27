package kaap.veiko.debuggerforker.commands.types;

import kaap.veiko.debuggerforker.commands.sets.virtualmachine.IDSizesReplyCommand;

import java.nio.ByteBuffer;

public class FrameID extends DataTypeBase {
    public FrameID(ByteBuffer buffer, IDSizesReplyCommand idSizes) {
        super(buffer, idSizes, IDSizesReplyCommand::getFrameIDSize);
    }
}
