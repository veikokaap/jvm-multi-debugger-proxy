package kaap.veiko.debuggerforker.commands.commandsets;

import kaap.veiko.debuggerforker.types.VMInformation;
import kaap.veiko.debuggerforker.types.jdwp.IdSizes;

public abstract class TestBase {

  private final VMInformation vmInformation = new VMInformation();
  private final IdSizes idSizes = new IdSizes(6,5,7,6,7);

  public TestBase() {
    vmInformation.setIdSizes(idSizes);
  }

  protected VMInformation getVmInformation() {
    return vmInformation;
  }

  protected IdSizes getIdSizes() {
    return idSizes;
  }
}
