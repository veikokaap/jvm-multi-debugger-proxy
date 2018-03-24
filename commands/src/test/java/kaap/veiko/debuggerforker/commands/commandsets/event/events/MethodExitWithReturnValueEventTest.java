package kaap.veiko.debuggerforker.commands.commandsets.event.events;

import org.junit.Test;

import kaap.veiko.debuggerforker.types.jdwp.EventKind;

public class MethodExitWithReturnValueEventTest extends EventTestBase {
  @Test
  public void testReadAndWriteEqualsOriginalEvent() throws ReflectiveOperationException {
    assertWrittenEventEqualsReadEvent(EventKind.METHOD_EXIT_WITH_RETURN_VALUE, MethodExitWithReturnValueEvent.create(
        randomInt(),
        randomThreadId(),
        randomLocation(),
        randomValue()
    ));
  }
}