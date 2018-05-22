package kaap.veiko.debuggerforker.commands.commandsets.eventrequest.filters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaap.veiko.debuggerforker.commands.parser.CommandDataReader;
import kaap.veiko.debuggerforker.types.DataReadException;
import kaap.veiko.debuggerforker.types.DataType;
import kaap.veiko.debuggerforker.types.DataWriter;

public abstract class EventRequestFilter implements DataType {

  private static Logger log = LoggerFactory.getLogger(EventRequestFilter.class);

  public static List<EventRequestFilter> readList(CommandDataReader reader) throws DataReadException {
    int length = reader.readInt();
    try {
      List<EventRequestFilter> list = new ArrayList<>(length);
      for (int i = 0; i < length; i++) {
        EventRequestFilter eventRequestFilter = readNext(reader);
        list.add(eventRequestFilter);
      }
      return list;
    } catch (RuntimeException e) {
      log.error("Failed to read EventRequestFilter list due to exception.", e);
      return Collections.emptyList();
    }
  }

  private static EventRequestFilter readNext(CommandDataReader reader) throws DataReadException {
    byte identifier = reader.readByte();
    switch (identifier) {
      case CountEventRequestFilter.IDENTIFIER:
        return CountEventRequestFilter.read(reader);
      case ConditionalEventRequestFilter.IDENTIFIER:
        return ConditionalEventRequestFilter.read(reader);
      case ThreadOnlyEventRequestFilter.IDENTIFIER:
        return ThreadOnlyEventRequestFilter.read(reader);
      case ClassOnlyEventRequestFilter.IDENTIFIER:
        return ClassOnlyEventRequestFilter.read(reader);
      case ClassMatchEventRequestFilter.IDENTIFIER:
        return ClassMatchEventRequestFilter.read(reader);
      case ClassExcludeEventRequestFilter.IDENTIFIER:
        return ClassExcludeEventRequestFilter.read(reader);
      case LocationOnlyEventRequestFilter.IDENTIFIER:
        return LocationOnlyEventRequestFilter.read(reader);
      case ExceptionOnlyEventRequestFilter.IDENTIFIER:
        return ExceptionOnlyEventRequestFilter.read(reader);
      case FieldOnlyEventRequestFilter.IDENTIFIER:
        return FieldOnlyEventRequestFilter.read(reader);
      case StepEventRequestFilter.IDENTIFIER:
        return StepEventRequestFilter.read(reader);
      case InstanceOnlyEventRequestFilter.IDENTIFIER:
        return InstanceOnlyEventRequestFilter.read(reader);
      case SourceNameMatchEventRequestFilter.IDENTIFIER:
        return SourceNameMatchEventRequestFilter.read(reader);
      default:
        throw new DataReadException("Failed to read EventRequestFilter with identifier '" + identifier + "'.");
    }
  }

  public static void writeList(DataWriter writer, List<EventRequestFilter> filters) {
    writer.writeInt(filters.size());

    for (EventRequestFilter filter : filters) {
      writer.writeType(filter);
    }
  }

}
