package ee.veikokaap.debugproxy.testframework.utils;

import org.checkerframework.checker.nullness.qual.Nullable;

public class BreakpointLocation {
  private final String className;
  private final int lineNumber;

  BreakpointLocation(String className, int lineNumber) {
    this.className = className;
    this.lineNumber = lineNumber;
  }

  public String getClassName() {
    return className;
  }

  public int getLineNumber() {
    return lineNumber;
  }

  @Override
  public boolean equals(@Nullable Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    BreakpointLocation location = (BreakpointLocation) o;

    if (lineNumber != location.lineNumber) {
      return false;
    }
    return className != null ? className.equals(location.className) : location.className == null;
  }

  @Override
  public int hashCode() {
    int result = className != null ? className.hashCode() : 0;
    result = 31 * result + lineNumber;
    return result;
  }

  @Override
  public String toString() {
    return "BreakpointLocation{" +
        "className='" + className + '\'' +
        ", lineNumber=" + lineNumber +
        '}';
  }
}