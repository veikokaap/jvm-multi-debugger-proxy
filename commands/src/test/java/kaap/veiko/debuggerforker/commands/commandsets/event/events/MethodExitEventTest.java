package kaap.veiko.debuggerforker.commands.commandsets.event.events;

import org.junit.Test;

import kaap.veiko.debuggerforker.types.jdwp.EventKind;

public class MethodExitEventTest extends EventTestBase {
  @Test
  public void testReadAndWriteEqualsOriginalEvent() throws ReflectiveOperationException {
    assertWrittenEventEqualsReadEvent(EventKind.METHOD_EXIT, MethodExitEvent.create(
        randomInt(),
        randomThreadId(),
        randomLocation()
    ));
  }
}