package kaap.veiko.debuggerforker.commands.commandsets.event.events;

import org.junit.Test;

import kaap.veiko.debuggerforker.types.DataReadException;
import kaap.veiko.debuggerforker.types.jdwp.EventKind;

public class MonitorContendedEnteredEventTest extends EventTestBase {
  @Test
  public void testReadAndWriteEqualsOriginalEvent() throws ReflectiveOperationException, DataReadException {
    assertWrittenEventEqualsReadEvent(EventKind.MONITOR_CONTENDED_ENTERED, MonitorContendedEnteredEvent.create(
        randomInt(),
        randomThreadId(),
        randomTaggedObjectId(),
        randomLocation()
    ));
  }
}
