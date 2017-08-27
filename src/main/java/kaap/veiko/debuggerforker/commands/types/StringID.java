package kaap.veiko.debuggerforker.commands.types;

import kaap.veiko.debuggerforker.commands.sets.virtualmachine.IDSizesReplyCommand;

import java.nio.ByteBuffer;

public class StringID extends ObjectID {
    public StringID(ByteBuffer buffer, IDSizesReplyCommand idSizes) {
        super(buffer, idSizes);
    }
}
