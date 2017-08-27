package kaap.veiko.debuggerforker.commands.types;

import kaap.veiko.debuggerforker.commands.sets.virtualmachine.IDSizesReplyCommand;

import java.nio.ByteBuffer;

public class ArrayID extends ObjectID {
    public ArrayID(ByteBuffer buffer, IDSizesReplyCommand idSizes) {
        super(buffer, idSizes);
    }
}
