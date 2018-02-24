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
        return VmStartEvent.Companion.read(reader);
      case SINGLE_STEP:
        return SingleStepEvent.Companion.read(reader);
      case BREAKPOINT:
        return BreakPointEvent.Companion.read(reader);
      case METHOD_ENTRY:
        return MethodEntryEvent.Companion.read(reader);
      case THREAD_START:
        return ThreadStartEvent.Companion.read(reader);
      case METHOD_EXIT:
        return MethodExitEvent.Companion.read(reader);
      case METHOD_EXIT_WITH_RETURN_VALUE:
        return MethodExitWithReturnValueEvent.Companion.read(reader);
      case MONITOR_CONTENDED_ENTER:
        return MonitorContendedEnterEvent.Companion.read(reader);
      case MONITOR_CONTENDED_ENTERED:
        return MonitorContendedEnteredEvent.Companion.read(reader);
      case MONITOR_WAIT:
        return MonitorWaitEvent.Companion.read(reader);
      case MONITOR_WAITED:
        return MonitorWaitedEvent.Companion.read(reader);
      case EXCEPTION:
        return ExceptionEvent.Companion.read(reader);
      case THREAD_DEATH:
        return ThreadDeathEvent.Companion.read(reader);
      case CLASS_PREPARE:
        return ClassPrepareEvent.Companion.read(reader);
      case CLASS_UNLOAD:
        return ClassUnloadEvent.Companion.read(reader);
      case FIELD_ACCESS:
        return FieldAccessEvent.Companion.read(reader);
      case FIELD_MODIFICATION:
        return FieldModificationEvent.Companion.read(reader);
      case VM_DEATH:
        return VmDeathEvent.Companion.read(reader);
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
