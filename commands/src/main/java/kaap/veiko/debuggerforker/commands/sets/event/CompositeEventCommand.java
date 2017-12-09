package kaap.veiko.debuggerforker.commands.sets.event;

import java.util.List;

import kaap.veiko.debuggerforker.commands.CommandBase;
import kaap.veiko.debuggerforker.commands.CommandVisitor;
import kaap.veiko.debuggerforker.commands.events.VirtualMachineEvent;
import kaap.veiko.debuggerforker.commands.parser.CommandDataReader;
import kaap.veiko.debuggerforker.commands.parser.CommandDataWriter;
import kaap.veiko.debuggerforker.commands.sets.CommandIdentifier;
import kaap.veiko.debuggerforker.packet.Packet;

public class CompositeEventCommand extends CommandBase {
  public static final CommandIdentifier COMMAND_IDENTIFIER = CommandIdentifier.COMPOSITE_EVENT_COMMAND;

  private final byte suspendPolicy;
  private final List<VirtualMachineEvent> events;

  public CompositeEventCommand(CommandDataReader reader, Packet packet) {
    super();
    this.suspendPolicy = reader.readByte();
    this.events = reader.readList(VirtualMachineEvent.PARSER);
    setPacket(packet);
  }

  @Override
  public void writeCommand(CommandDataWriter writer) {
    writer.writeByte(suspendPolicy);
    writer.writeList(VirtualMachineEvent.PARSER, events);
  }

  @Override
  protected CommandIdentifier getCommandIdentifier() {
    return COMMAND_IDENTIFIER;
  }

  public byte getSuspendPolicy() {
    return suspendPolicy;
  }

  public List<VirtualMachineEvent> getEvents() {
    return events;
  }

  @Override
  public <T> T visit(CommandVisitor<T> visitor) {
    return visitor.visit(this);
  }

  @Override
  public String toString() {
    return "CompositeEventCommand{" +
        "suspendPolicy=" + suspendPolicy +
        ", events=" + events +
        '}';
  }

}
