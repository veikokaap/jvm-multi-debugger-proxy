package kaap.veiko.debuggerforker.types;

import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import kaap.veiko.debuggerforker.types.jdwp.IdSizes;

public class VMInformation {

  private @MonotonicNonNull IdSizes idSizes = null;

  public @Nullable IdSizes getIdSizes() {
    return idSizes;
  }

  @EnsuresNonNull("this.idSizes")
  public void setIdSizes(IdSizes idSizes) {
    this.idSizes = idSizes;
  }
}
