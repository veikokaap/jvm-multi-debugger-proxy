package kaap.veiko.debuggerforker.commands.types;

import kaap.veiko.debuggerforker.commands.sets.virtualmachine.IDSizesReplyCommand;

import java.nio.ByteBuffer;

public class ReferenceTypeID extends ObjectID {
    public ReferenceTypeID(ByteBuffer buffer, IDSizesReplyCommand idSizes) {
        super(buffer, idSizes);
    }
}
