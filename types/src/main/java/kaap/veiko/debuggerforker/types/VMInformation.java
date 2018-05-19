package kaap.veiko.debuggerforker.types;

import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import kaap.veiko.debuggerforker.types.jdwp.IdSizes;

public class VMInformation {

  private @MonotonicNonNull IdSizes idSizes = null;

  public @Nullable IdSizes getIdSizes() {
    return idSizes;
  }

  public void setIdSizes(@NonNull IdSizes idSizes) {
    this.idSizes = idSizes;
  }
}
