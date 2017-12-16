package kaap.veiko.debuggerforker.commands.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import kaap.veiko.debuggerforker.commands.parser.CommandDataReader;
import kaap.veiko.debuggerforker.commands.parser.DataTypeArrayParser;
import kaap.veiko.debuggerforker.types.DataType;
import kaap.veiko.debuggerforker.types.DataWriter;
import kaap.veiko.debuggerforker.types.jdwp.EventKind;

public abstract class VirtualMachineEvent implements DataType {
  public static final DataTypeArrayParser<VirtualMachineEvent> PARSER = new VirtualMachineEventArrayParser();

  private static class VirtualMachineEventArrayParser implements DataTypeArrayParser<VirtualMachineEvent> {

    private final Map<EventKind, Function<CommandDataReader, VirtualMachineEvent>> parseMap = new HashMap<>();

    private VirtualMachineEventArrayParser() {
      parseMap.put(VMStartEvent.EVENT_KIND_IDENTIFIER, VMStartEvent::new);
      parseMap.put(ThreadStartEvent.EVENT_KIND_IDENTIFIER, ThreadStartEvent::new);
      parseMap.put(ClassPrepareEvent.EVENT_KIND_IDENTIFIER, ClassPrepareEvent::new);
      parseMap.put(BreakPointEvent.EVENT_KIND_IDENTIFIER, BreakPointEvent::new);
    }

    @Override
    public List<VirtualMachineEvent> readList(CommandDataReader reader) throws Exception {
      int length = reader.readInt();
      List<VirtualMachineEvent> events = new ArrayList<>();

      for (int i = 0; i < length; i++) {
        Optional<VirtualMachineEvent> event = Optional.of(EventKind.read(reader))
            .map(parseMap::get)
            .map(f -> f.apply(reader));

        if (event.isPresent()) {
          events.add(event.get());
        }
        else {
          throw new Exception("Couldn't find the correct VirtualMachineEvent");
        }
      }

      return events;
    }

    @Override
    public void writeList(DataWriter writer, List<VirtualMachineEvent> events) {
      writer.writeInt(events.size());

      for (VirtualMachineEvent event : events) {
        writer.writeType(event);
      }
    }
  }
}
