package kaap.veiko.debuggerforker.commands.types;

import kaap.veiko.debuggerforker.commands.sets.virtualmachine.IDSizesReplyCommand;

import java.nio.ByteBuffer;

public class ObjectID extends DataTypeBase {
    public ObjectID(ByteBuffer buffer, IDSizesReplyCommand idSizes) {
        super(buffer, idSizes, IDSizesReplyCommand::getObjectIDSize);
    }
}
