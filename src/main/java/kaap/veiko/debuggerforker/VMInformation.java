package kaap.veiko.debuggerforker;

import kaap.veiko.debuggerforker.commands.sets.virtualmachine.IDSizesReply;

public class VMInformation {
  private IDSizesReply idSizes;

  public IDSizesReply getIdSizes() {
    return idSizes;
  }

  public void setIdSizes(IDSizesReply idSizes) {
    this.idSizes = idSizes;
  }
}
