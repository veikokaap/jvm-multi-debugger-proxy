package kaap.veiko.debuggerforker.commands.util;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.commands.Command;
import kaap.veiko.debuggerforker.commands.parser.CommandDataWriter;
import kaap.veiko.debuggerforker.types.VMInformation;

public class CommandDataUtil {
  public static byte[] getCommandDataBytes(Command command, VMInformation vmInformation) {
    ByteBuffer buffer = ByteBuffer.allocate(1024);
    CommandDataWriter writer = new CommandDataWriter(buffer, vmInformation);
    command.writeCommand(writer);

    byte[] bytes = new byte[buffer.position()];
    buffer.get(bytes);

    return bytes;
  }
}
