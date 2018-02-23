package kaap.veiko.debuggerforker.commands.commandsets.event.events;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaap.veiko.debuggerforker.commands.parser.CommandDataReader;
import kaap.veiko.debuggerforker.types.DataType;
import kaap.veiko.debuggerforker.types.DataWriter;
import kaap.veiko.debuggerforker.types.jdwp.EventKind;

public abstract class VirtualMachineEvent implements DataType {

  private static Logger log = LoggerFactory.getLogger(VirtualMachineEvent.class);

  public static List<VirtualMachineEvent> readList(CommandDataReader reader) {
    int length = reader.readInt();
    try {
      return IntStream.range(0, length)
          .mapToObj(i -> readNext(reader))
          .collect(Collectors.toList());
    } catch (RuntimeException e) {
      log.error("Failed to read VirtualMachineEvent list due to exception.", e);
      return Collections.emptyList();
    }
  }

  private static VirtualMachineEvent readNext(CommandDataReader reader) throws RuntimeException {
    EventKind eventKind = EventKind.read(reader);
    switch (eventKind) {
      case VM_START:
        return VMStartEvent.read(reader);
      case BREAKPOINT:
        return BreakPointEvent.read(reader);
      case THREAD_START:
        return ThreadStartEvent.read(reader);
      case CLASS_PREPARE:
        return ClassPrepareEvent.read(reader);
      case SINGLE_STEP:
      case METHOD_ENTRY:
      case METHOD_EXIT:
      case METHOD_EXIT_WITH_RETURN_VALUE:
      case MONITOR_CONTENDED_ENTER:
      case MONITOR_CONTENDED_ENTERED:
      case MONITOR_WAIT:
      case MONITOR_WAITED:
      case EXCEPTION:
      case THREAD_DEATH:
      case CLASS_UNLOAD:
      case FIELD_ACCESS:
      case FIELD_MODIFICATION:
      case VM_DEATH:
      default:
        throw new RuntimeException("Failed to parse");
    }
  }

  public static void writeList(DataWriter writer, List<VirtualMachineEvent> events) {
    writer.writeInt(events.size());

    for (VirtualMachineEvent event : events) {
      writer.writeType(event);
    }
  }

}
