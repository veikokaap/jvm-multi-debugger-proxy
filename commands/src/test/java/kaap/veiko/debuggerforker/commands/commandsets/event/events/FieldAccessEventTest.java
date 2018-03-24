package kaap.veiko.debuggerforker.commands.commandsets.event.events;

import org.junit.Test;

import kaap.veiko.debuggerforker.types.jdwp.EventKind;

public class FieldAccessEventTest extends EventTestBase {

  @Test
  public void testReadAndWriteEqualsOriginalEvent() throws ReflectiveOperationException {
    assertWrittenEventEqualsReadEvent(EventKind.FIELD_ACCESS, FieldAccessEvent.create(
            randomInt(),
            randomThreadId(),
            randomLocation(),
            randomByte(),
            randomReferenceTypeId(),
            randomFieldId(),
            randomTaggedObjectId()
    ));
  }
}