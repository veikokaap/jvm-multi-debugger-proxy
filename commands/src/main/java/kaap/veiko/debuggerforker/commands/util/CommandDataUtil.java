package kaap.veiko.debuggerforker.commands.util;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.commands.Command;
import kaap.veiko.debuggerforker.packet.writer.ByteBufferDataWriter;
import kaap.veiko.debuggerforker.types.VMInformation;

public class CommandDataUtil {
  public static byte[] getCommandDataBytes(Command command, VMInformation vmInformation) {
    int bufferSize = 1024;

    ByteBuffer buffer = null;
    while (buffer == null) {
      try {
        buffer = writeCommandBytesToBuffer(command, vmInformation, bufferSize);
      }
      catch (BufferOverflowException e) {
        // Buffer not big enough, double its size and try again
        bufferSize *= 2;
        buffer = null;
      }
    }

    byte[] bytes = new byte[buffer.position()];
    buffer.flip();
    buffer.get(bytes);

    return bytes;
  }

  private static ByteBuffer writeCommandBytesToBuffer(Command command, VMInformation vmInformation, int bufferSize) {
    ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
    command.writeCommand(new ByteBufferDataWriter(buffer, vmInformation));
    return buffer;
  }
}
