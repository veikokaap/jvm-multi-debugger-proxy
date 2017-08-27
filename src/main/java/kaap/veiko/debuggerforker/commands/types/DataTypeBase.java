package kaap.veiko.debuggerforker.commands.types;

import kaap.veiko.debuggerforker.commands.sets.virtualmachine.IDSizesReplyCommand;
import kaap.veiko.debuggerforker.utils.ByteBufferUtil;

import java.nio.ByteBuffer;
import java.util.function.Function;

public abstract class DataTypeBase implements DataType {

    private final long value;

    public DataTypeBase(ByteBuffer buffer, IDSizesReplyCommand idSizes, Function<IDSizesReplyCommand, Integer> sizeFunction) {
        if (idSizes != null) {
            int size = sizeFunction.apply(idSizes);
            value = ByteBufferUtil.getLong(buffer, size);
        } else {
            System.out.println("WARN: Parsing value without knowing its size in bytes. Assuming size is 8 bytes.");
            value = buffer.getLong();
        }
    }

    @Override
    public long asLong() {
        return value;
    }

    @Override
    public String toString() {
        return "DataTypeBase{" +
                "value=" + value +
                '}';
    }
}
