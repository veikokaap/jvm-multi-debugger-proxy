package kaap.veiko.debuggerforker.commands.commandsets.event;

import java.util.List;

import kaap.veiko.debuggerforker.commands.CommandBase;
import kaap.veiko.debuggerforker.commands.CommandVisitor;
import kaap.veiko.debuggerforker.commands.MutableCommandPacket;
import kaap.veiko.debuggerforker.commands.commandsets.event.events.VirtualMachineEvent;
import kaap.veiko.debuggerforker.commands.parser.CommandDataReader;
import kaap.veiko.debuggerforker.commands.commandsets.CommandIdentifier;
import kaap.veiko.debuggerforker.commands.util.CommandDataUtil;
import kaap.veiko.debuggerforker.packet.CommandPacket;
import kaap.veiko.debuggerforker.types.DataReadException;
import kaap.veiko.debuggerforker.types.DataWriter;
import kaap.veiko.debuggerforker.types.VMInformation;

public class CompositeEventCommand extends CommandBase<CommandPacket> {
  private static final CommandIdentifier COMMAND_IDENTIFIER = CommandIdentifier.COMPOSITE_EVENT_COMMAND;

  private final byte suspendPolicy;
  private final List<VirtualMachineEvent> events;

  public static CompositeEventCommand create(int packetId, byte suspendPolicy, List<VirtualMachineEvent> events, VMInformation vmInformation) {
    MutableCommandPacket packet = MutableCommandPacket.create(packetId, COMMAND_IDENTIFIER);
    CompositeEventCommand command = new CompositeEventCommand(packet, suspendPolicy, events);
    packet.setData(CommandDataUtil.getCommandDataBytes(command, vmInformation));

    return command;
  }

  public static CompositeEventCommand read(CommandDataReader reader) throws DataReadException {
    byte suspendPolicy = reader.readByte();
    List<VirtualMachineEvent> events = VirtualMachineEvent.readList(reader);

    return new CompositeEventCommand((CommandPacket) reader.getPacket(), suspendPolicy, events);
  }

  private CompositeEventCommand(CommandPacket packet, byte suspendPolicy, List<VirtualMachineEvent> events) {
    super(packet, CommandIdentifier.COMPOSITE_EVENT_COMMAND);
    this.suspendPolicy = suspendPolicy;
    this.events = events;
  }

  @Override
  public void writeCommand(DataWriter writer) {
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
