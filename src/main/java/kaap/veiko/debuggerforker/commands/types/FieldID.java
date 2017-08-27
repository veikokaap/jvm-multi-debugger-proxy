package kaap.veiko.debuggerforker.commands.types;

import kaap.veiko.debuggerforker.commands.sets.virtualmachine.IDSizesReplyCommand;

import java.nio.ByteBuffer;

public class FieldID extends DataTypeBase {
    public FieldID(ByteBuffer buffer, IDSizesReplyCommand idSizes) {
        super(buffer, idSizes, IDSizesReplyCommand::getFieldIDSize);
    }
}
