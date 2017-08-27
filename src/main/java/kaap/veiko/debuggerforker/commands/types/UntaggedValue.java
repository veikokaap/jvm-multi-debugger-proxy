package kaap.veiko.debuggerforker.commands.types;

import kaap.veiko.debuggerforker.commands.sets.virtualmachine.IDSizesReplyCommand;

import java.nio.ByteBuffer;

public class UntaggedValue extends Value {
    public UntaggedValue(ByteBuffer byteBuffer, IDSizesReplyCommand idSizes, byte typeTag) {
        super(byteBuffer, idSizes, typeTag);
    }
}
