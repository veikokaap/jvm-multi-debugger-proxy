package kaap.veiko.debuggerforker.commands.commandsets.event.events;

import org.junit.Test;

import kaap.veiko.debuggerforker.types.jdwp.ClassId;
import kaap.veiko.debuggerforker.types.jdwp.EventKind;
import kaap.veiko.debuggerforker.types.jdwp.Location;
import kaap.veiko.debuggerforker.types.jdwp.MethodId;
import kaap.veiko.debuggerforker.types.jdwp.ObjectId;
import kaap.veiko.debuggerforker.types.jdwp.TaggedObjectId;
import kaap.veiko.debuggerforker.types.jdwp.ThreadId;

public class ExceptionEventTest extends EventTestBase {
  @Test
  public void testReadAndWriteEqualsOriginalEvent() throws ReflectiveOperationException {
    assertWrittenEventEqualsReadEvent(EventKind.EXCEPTION, ExceptionEvent.create(
        randomInt(),
        randomThreadId(),
        randomLocation(),
        randomTaggedObjectId(),
        randomLocation()
    ));
  }
}