package kaap.veiko.debuggerforker.commands.types;

import kaap.veiko.debuggerforker.commands.sets.virtualmachine.IDSizesReplyCommand;

import java.nio.ByteBuffer;

public class ClassLoaderID extends ObjectID {
    public ClassLoaderID(ByteBuffer buffer, IDSizesReplyCommand idSizes) {
        super(buffer, idSizes);
    }
}
