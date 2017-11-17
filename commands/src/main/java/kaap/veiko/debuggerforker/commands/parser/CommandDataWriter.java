package kaap.veiko.debuggerforker.commands.parser;

import java.nio.ByteBuffer;
import java.util.List;

import kaap.veiko.debuggerforker.packet.ByteBufferDataWriter;
import kaap.veiko.debuggerforker.types.DataType;
import kaap.veiko.debuggerforker.types.VMInformation;

public class CommandDataWriter extends ByteBufferDataWriter {
  public CommandDataWriter(ByteBuffer byteBuffer, VMInformation vmInformation) {
    super(byteBuffer, vmInformation);
  }

  public <T extends DataType> void writeList(DataTypeArrayParser<T> parser, List<T> list) {
    parser.writeList(this, list);
  }
}
