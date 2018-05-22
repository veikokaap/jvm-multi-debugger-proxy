package kaap.veiko.debuggerforker.commands.commandsets.event.events;

import org.junit.Test;

import kaap.veiko.debuggerforker.types.DataReadException;
import kaap.veiko.debuggerforker.types.jdwp.EventKind;

public class MonitorWaitEventTest extends EventTestBase {
  @Test
  public void testReadAndWriteEqualsOriginalEvent() throws ReflectiveOperationException, DataReadException {
    assertWrittenEventEqualsReadEvent(EventKind.MONITOR_WAIT, MonitorWaitEvent.create(
        randomInt(),
        randomThreadId(),
        randomTaggedObjectId(),
        randomLocation(),
        randomLong()
    ));
  }
}