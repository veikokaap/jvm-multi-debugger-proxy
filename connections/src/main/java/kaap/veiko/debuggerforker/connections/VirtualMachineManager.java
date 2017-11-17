package kaap.veiko.debuggerforker.connections;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import kaap.veiko.debuggerforker.packet.PacketStream;
import kaap.veiko.debuggerforker.packet.VirtualMachinePacketStream;
import kaap.veiko.debuggerforker.types.VMInformation;

public class VirtualMachineManager implements Connection {

  private final PacketStream packetStream;
  private final VMInformation vmInformation;

  public VirtualMachineManager(SocketChannel socketChannel) throws IOException {
    this.packetStream = new VirtualMachinePacketStream(socketChannel);
    this.vmInformation = new VMInformation();
  }

  public PacketStream getPacketStream() {
    return packetStream;
  }

  @Override
  public void close() throws IOException {
    packetStream.close();
  }

//  public void removeBreakPoint(int requestId)  {
//    Packet command = new ClearEventRequestCommand(EventKind.BREAKPOINT, requestId), false);
//    try {
//      packetStream.write(command);
//    }
//    catch (IOException e) {
//      e.printStackTrace();
//    }
//  }
}
