package kaap.veiko.debuggerforker;

import kaap.veiko.debuggerforker.commands.sets.virtualmachine.IDSizesReplyCommand;

public class VMInformation {
  private IDSizesReplyCommand idSizes;

  public IDSizesReplyCommand getIdSizes() {
    return idSizes;
  }

  public void setIdSizes(IDSizesReplyCommand idSizes) {
    this.idSizes = idSizes;
  }
}
