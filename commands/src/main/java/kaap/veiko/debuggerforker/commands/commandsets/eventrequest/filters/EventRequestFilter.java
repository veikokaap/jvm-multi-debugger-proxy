package kaap.veiko.debuggerforker.commands.commandsets.eventrequest.filters;

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

public abstract class EventRequestFilter implements DataType {
  public static final DataTypeArrayParser<EventRequestFilter> PARSER = new EventRequestFilterArrayParser();

  private static class EventRequestFilterArrayParser implements DataTypeArrayParser<EventRequestFilter> {

    private final Map<Byte, Function<CommandDataReader, EventRequestFilter>> parseMap = new HashMap<>();

    public EventRequestFilterArrayParser() {
      parseMap.put(EventRequestLocationFilter.IDENTIFIER, EventRequestLocationFilter::new);
    }

    @Override
    public List<EventRequestFilter> readList(CommandDataReader reader) throws Exception {
      int length = reader.readInt();
      List<EventRequestFilter> filters = new ArrayList<>();

      for (int i = 0; i < length; i++) {
        Optional<EventRequestFilter> filter = Optional.of(reader.readByte())
            .map(parseMap::get)
            .map(f -> f.apply(reader));

        if (filter.isPresent()) {
          filters.add(filter.get());
        }
        else {
          throw new Exception("Couldn't find the correct VirtualMachineEvent");
        }
      }

      return filters;
    }

    @Override
    public void writeList(DataWriter writer, List<EventRequestFilter> filters) {
      writer.writeInt(filters.size());

      for (EventRequestFilter filter : filters) {
        writer.writeType(filter);
      }
    }
  }
}
