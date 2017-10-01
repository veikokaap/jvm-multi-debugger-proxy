package kaap.veiko.debuggerforker.commands.sets.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kaap.veiko.debuggerforker.commands.Command;
import kaap.veiko.debuggerforker.commands.parser.annotations.JDWPArray;
import kaap.veiko.debuggerforker.commands.parser.annotations.JDWPCommand;
import kaap.veiko.debuggerforker.commands.parser.annotations.JDWPCommandConstructor;
import kaap.veiko.debuggerforker.commands.sets.CommandIdentifier;
import kaap.veiko.debuggerforker.events.VirtualMachineEvent;

@JDWPCommand(CommandIdentifier.COMPOSITE_EVENT_COMMAND)
public class CompositeEvent implements Command {

  private final byte suspendPolicy;
  private final List<VirtualMachineEvent> events;

  @JDWPCommandConstructor
  public CompositeEvent(byte suspendPolicy, @JDWPArray(counterType = Integer.class) VirtualMachineEvent[] events) {
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
    return "CompositeEvent{" +
        "suspendPolicy=" + suspendPolicy +
        ", events=" + events +
        '}';
  }
}
