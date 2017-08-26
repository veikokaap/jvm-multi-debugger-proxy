package kaap.veiko.debuggerforker.commands;

import kaap.veiko.debuggerforker.commands.parser.CommandType;
import kaap.veiko.debuggerforker.commands.parser.JDWPCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JDWPCommand(commandSet = 64, command = 100, commandType = CommandType.COMMAND)
public class CompositeEventCommand implements Command {

    private final byte suspendPolicy;
    private final List<VirtualMachineEvent> events;

    public CompositeEventCommand(byte suspendPolicy, int eventCount, VirtualMachineEvent[] events) {
        this.suspendPolicy = suspendPolicy;
        this.events = new ArrayList<>(Arrays.asList(events));
    }

    @Override
    public String toString() {
        return "CompositeEventCommand{" +
                "suspendPolicy=" + suspendPolicy +
                ", events=" + events +
                '}';
    }
}
