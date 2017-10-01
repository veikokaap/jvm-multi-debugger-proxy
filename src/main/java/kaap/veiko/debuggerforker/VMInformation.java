package kaap.veiko.debuggerforker;

import kaap.veiko.debuggerforker.commands.sets.virtualmachine.IdSizesReply;

public class VMInformation {
  private IdSizesReply idSizes;

  public IdSizesReply getIdSizes() {
    return idSizes;
  }

  public void setIdSizes(IdSizesReply idSizes) {
    this.idSizes = idSizes;
  }
}
