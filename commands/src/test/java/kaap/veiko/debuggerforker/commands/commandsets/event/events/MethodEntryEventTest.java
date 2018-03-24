package kaap.veiko.debuggerforker.commands.commandsets.event.events;

import org.junit.Test;

import kaap.veiko.debuggerforker.types.jdwp.EventKind;

public class MethodEntryEventTest extends EventTestBase {
  @Test
  public void testReadAndWriteEqualsOriginalEvent() throws ReflectiveOperationException {
    assertWrittenEventEqualsReadEvent(EventKind.METHOD_ENTRY, MethodEntryEvent.create(
        randomInt(),
        randomThreadId(),
        randomLocation()
    ));
  }
}