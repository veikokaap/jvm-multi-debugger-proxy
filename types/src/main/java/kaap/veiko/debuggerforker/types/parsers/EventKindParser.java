package kaap.veiko.debuggerforker.types.parsers;

import java.lang.reflect.Parameter;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Optional;

import kaap.veiko.debuggerforker.types.EventKind;

public class EventKindParser implements TypeParser<EventKind> {
  @Override
  public boolean hasCorrectType(Class<?> type) {
    return EventKind.class.isAssignableFrom(type);
  }

  @Override
  public EventKind parse(ByteBuffer byteBuffer, Parameter parameter) throws ReflectiveOperationException {
    byte aByte = byteBuffer.get();

    Optional<EventKind> first = Arrays.stream(EventKind.values())
        .filter(eventKind -> eventKind.getId() == aByte)
        .findFirst();

    if (!first.isPresent()) {
      throw new RuntimeException("Failed to find correct EventKind");
    }

    return first.get();
  }

  @Override
  public void putToBuffer(ByteBuffer buffer, EventKind value) {
    buffer.put(value.getId());
  }
}
