package kaap.veiko.debuggerforker.commands.sets.event;

import kaap.veiko.debuggerforker.commands.Command;
import kaap.veiko.debuggerforker.commands.parser.CommandType;
import kaap.veiko.debuggerforker.commands.parser.annotations.Array;
import kaap.veiko.debuggerforker.commands.parser.annotations.ArrayCounter;
import kaap.veiko.debuggerforker.commands.parser.annotations.JDWPCommand;
import kaap.veiko.debuggerforker.commands.parser.annotations.JDWPCommandConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JDWPCommand(commandSet = 64, command = 100, commandType = CommandType.COMMAND)
public class CompositeEventCommand implements Command {

    private final byte suspendPolicy;
    private final List<VirtualMachineEvent> events;

    @JDWPCommandConstructor
    public CompositeEventCommand(byte suspendPolicy, @ArrayCounter int eventCount, @Array VirtualMachineEvent[] events) {
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
