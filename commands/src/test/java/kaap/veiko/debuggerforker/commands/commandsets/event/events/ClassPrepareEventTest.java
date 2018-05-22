package kaap.veiko.debuggerforker.commands.commandsets.event.events;

import org.junit.Test;

import kaap.veiko.debuggerforker.types.DataReadException;
import kaap.veiko.debuggerforker.types.jdwp.EventKind;
import kaap.veiko.debuggerforker.types.jdwp.ReferenceTypeId;
import kaap.veiko.debuggerforker.types.jdwp.ThreadId;

public class ClassPrepareEventTest extends EventTestBase {
  @Test
  public void testReadAndWriteEqualsOriginalEvent() throws ReflectiveOperationException, DataReadException {
    assertWrittenEventEqualsReadEvent(EventKind.CLASS_PREPARE, ClassPrepareEvent.create(
        randomInt(),
        randomThreadId(),
        randomByte(),
        randomReferenceTypeId(),
        randomString(),
        randomInt()
    ));
  }
}