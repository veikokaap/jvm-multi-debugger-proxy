package kaap.veiko.debuggerforker.commands.sets.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kaap.veiko.debuggerforker.commands.Command;
import kaap.veiko.debuggerforker.commands.parser.annotations.JdwpArray;
import kaap.veiko.debuggerforker.commands.parser.annotations.JdwpCommand;
import kaap.veiko.debuggerforker.commands.parser.annotations.JdwpCommandConstructor;
import kaap.veiko.debuggerforker.commands.sets.CommandIdentifier;
import kaap.veiko.debuggerforker.events.VirtualMachineEvent;

@JdwpCommand(CommandIdentifier.COMPOSITE_EVENT_COMMAND)
public class CompositeEventCommand implements Command {

  private final byte suspendPolicy;
  private final List<VirtualMachineEvent> events;

  @JdwpCommandConstructor
  public CompositeEventCommand(byte suspendPolicy, @JdwpArray(counterType = Integer.class) VirtualMachineEvent[] events) {
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
  public String toString() {
    return "CompositeEventCommand{" +
        "suspendPolicy=" + suspendPolicy +
        ", events=" + events +
        '}';
  }
}
