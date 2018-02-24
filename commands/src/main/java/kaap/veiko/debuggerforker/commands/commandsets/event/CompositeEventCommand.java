package kaap.veiko.debuggerforker.commands.commandsets.event;

import java.util.List;

import kaap.veiko.debuggerforker.commands.CommandBase;
import kaap.veiko.debuggerforker.commands.CommandVisitor;
import kaap.veiko.debuggerforker.commands.MutableCommandPacket;
import kaap.veiko.debuggerforker.commands.commandsets.event.events.VirtualMachineEvent;
import kaap.veiko.debuggerforker.commands.parser.CommandDataReader;
import kaap.veiko.debuggerforker.commands.parser.CommandDataWriter;
import kaap.veiko.debuggerforker.commands.commandsets.CommandIdentifier;
import kaap.veiko.debuggerforker.commands.util.CommandDataUtil;
import kaap.veiko.debuggerforker.packet.Packet;
import kaap.veiko.debuggerforker.types.VMInformation;

public class CompositeEventCommand extends CommandBase {
  private static final CommandIdentifier COMMAND_IDENTIFIER = CommandIdentifier.COMPOSITE_EVENT_COMMAND;

  private final byte suspendPolicy;
  private final List<VirtualMachineEvent> events;

  public static CompositeEventCommand create(int packetId, VMInformation vmInformation, byte suspendPolicy, List<VirtualMachineEvent> events) {
    MutableCommandPacket packet = MutableCommandPacket.create(packetId, COMMAND_IDENTIFIER);
    CompositeEventCommand command = new CompositeEventCommand(packet, suspendPolicy, events);
    packet.setData(CommandDataUtil.getCommandDataBytes(command, vmInformation));

    return command;
  }

  public static CompositeEventCommand read(CommandDataReader reader) {
    byte suspendPolicy = reader.readByte();
    List<VirtualMachineEvent> events = VirtualMachineEvent.readList(reader);

    return new CompositeEventCommand(reader.getPacket(), suspendPolicy, events);
  }

  private CompositeEventCommand(Packet packet, byte suspendPolicy, List<VirtualMachineEvent> events) {
    super(packet, CommandIdentifier.COMPOSITE_EVENT_COMMAND);
    this.suspendPolicy = suspendPolicy;
    this.events = events;
  }

  @Override
  public void writeCommand(CommandDataWriter writer) {
    writer.writeByte(suspendPolicy);
    VirtualMachineEvent.writeList(writer, events);
  }

  public byte getSuspendPolicy() {
    return suspendPolicy;
  }

  public List<VirtualMachineEvent> getEvents() {
    return events;
  }

  @Override
  public void visit(CommandVisitor visitor) {
    visitor.visit(this);
  }

  @Override
  public String toString() {
    return "CompositeEventCommand{" +
        "suspendPolicy=" + suspendPolicy +
        ", events=" + events +
        '}';
  }

}