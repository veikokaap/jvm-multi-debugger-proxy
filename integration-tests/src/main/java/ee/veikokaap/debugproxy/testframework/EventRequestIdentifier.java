package ee.veikokaap.debugproxy.testframework;

import java.lang.reflect.Field;
import com.sun.jdi.request.EventRequest;

final class EventRequestIdentifier {
  private final Class<?> requestClass;
  private final int requestId;

  static EventRequestIdentifier fromEventRequest(EventRequest request) throws ReflectiveOperationException {
    Field field = getDeclaredField(request.getClass(), "id");
    field.setAccessible(true);
    int requestId = field.getInt(request);
    return new EventRequestIdentifier(request.getClass(), requestId);
  }

  private EventRequestIdentifier(Class<?> requestClass, int requestId) {
    this.requestClass = requestClass;
    this.requestId = requestId;
  }

  public int getRequestId() {
    return requestId;
  }

  public Class<?> getRequestClass() {
    return requestClass;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    EventRequestIdentifier that = (EventRequestIdentifier) o;

    if (requestId != that.requestId) {
      return false;
    }
    return requestClass != null ? requestClass.equals(that.requestClass) : that.requestClass == null;
  }

  @Override
  public int hashCode() {
    int result = requestClass != null ? requestClass.hashCode() : 0;
    result = 31 * result + requestId;
    return result;
  }

  @Override
  public String toString() {
    return "EventRequestIdentifier{" +
        "requestClass=" + requestClass +
        ", requestId=" + requestId +
        '}';
  }

  private static Field getDeclaredField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
    if (clazz == null) {
      throw new NoSuchFieldException(fieldName);
    }

    try {
      return clazz.getDeclaredField(fieldName);
    }
    catch (Exception ignored) {
      return getDeclaredField(clazz.getSuperclass(), fieldName);
    }
  }
}