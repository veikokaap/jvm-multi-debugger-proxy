package kaap.veiko.debuggerforker.commands.commandsets.event.events;

import org.junit.Test;

import kaap.veiko.debuggerforker.types.jdwp.EventKind;

public class MonitorContendedEnterEventTest extends EventTestBase {
  @Test
  public void testReadAndWriteEqualsOriginalEvent() throws ReflectiveOperationException {
    assertWrittenEventEqualsReadEvent(EventKind.MONITOR_CONTENDED_ENTER, MonitorContendedEnterEvent.create(
        randomInt(),
        randomThreadId(),
        randomTaggedObjectId(),
        randomLocation()
    ));
  }
}