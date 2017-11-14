package kaap.veiko.debuggerforker.connections;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import kaap.veiko.debuggerforker.types.VMInformation;
import kaap.veiko.debuggerforker.types.EventKind;
import kaap.veiko.debuggerforker.commands.sets.eventrequest.ClearEventRequestCommand;
import kaap.veiko.debuggerforker.packet.Packet;
import kaap.veiko.debuggerforker.packet.PacketCreator;
import kaap.veiko.debuggerforker.packet.PacketStream;
import kaap.veiko.debuggerforker.packet.VirtualMachinePacketStream;

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

  public void removeBreakPoint(int requestId)  {
    Packet command = new PacketCreator(vmInformation).createCommand(-1, new ClearEventRequestCommand(EventKind.BREAKPOINT, requestId), false);
    try {
      packetStream.write(command);
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }
}
