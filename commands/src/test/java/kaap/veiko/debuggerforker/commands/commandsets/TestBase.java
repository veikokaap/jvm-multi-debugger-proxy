package kaap.veiko.debuggerforker.commands.commandsets;

import org.junit.Before;

import kaap.veiko.debuggerforker.types.VMInformation;
import kaap.veiko.debuggerforker.types.jdwp.IdSizes;

public abstract class TestBase {
  private final VMInformation vmInformation = new VMInformation();

  @Before
  public void setup() {
    vmInformation.setIdSizes(new IdSizes(6,5,7,6,7));
  }

  protected VMInformation getVmInformation() {
    return vmInformation;
  }
}
