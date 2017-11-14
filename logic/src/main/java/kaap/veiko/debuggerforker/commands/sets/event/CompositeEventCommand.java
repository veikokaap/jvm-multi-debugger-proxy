package kaap.veiko.debuggerforker.commands.sets.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kaap.veiko.debuggerforker.commands.CommandBase;
import kaap.veiko.debuggerforker.commands.parser.annotations.JdwpCommand;
import kaap.veiko.debuggerforker.commands.parser.annotations.JdwpCommandConstructor;
import kaap.veiko.debuggerforker.commands.sets.CommandIdentifier;
import kaap.veiko.debuggerforker.events.VirtualMachineEvent;

@JdwpCommand(CommandIdentifier.COMPOSITE_EVENT_COMMAND)
public class CompositeEventCommand extends CommandBase {

  private final byte suspendPolicy;
  private final List<VirtualMachineEvent> events;

  @JdwpCommandConstructor
  public CompositeEventCommand(byte suspendPolicy, VirtualMachineEvent[] events) {
    this.suspendPolicy = suspendPolicy;
    this.events = new ArrayList<>(Arrays.asList(events));
  }

  public byte getSuspendPolicy() {
    return suspendPolicy;
  }

  public List<VirtualMachineEvent> getEvents() {
    return events;
  }

  @Override
  public List<Object> getPacketValues() {
    return Arrays.asList(suspendPolicy, events.toArray(new VirtualMachineEvent[0]));
  }

  @Override
  public String toString() {
    return "CompositeEventCommand{" +
        "suspendPolicy=" + suspendPolicy +
        ", events=" + events +
        '}';
  }
}
