package kaap.veiko.debuggerforker.commands.types;

import kaap.veiko.debuggerforker.commands.sets.virtualmachine.IDSizesReplyCommand;

import java.nio.ByteBuffer;

public class MethodID extends DataTypeBase {
    public MethodID(ByteBuffer buffer, IDSizesReplyCommand idSizes) {
        super(buffer, idSizes, IDSizesReplyCommand::getMethodIDSize);
    }
}
