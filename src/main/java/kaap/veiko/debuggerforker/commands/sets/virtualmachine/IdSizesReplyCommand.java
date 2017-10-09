package kaap.veiko.debuggerforker.commands.sets.virtualmachine;

import kaap.veiko.debuggerforker.commands.Command;
import kaap.veiko.debuggerforker.commands.IdSizes;
import kaap.veiko.debuggerforker.commands.parser.annotations.JdwpCommand;
import kaap.veiko.debuggerforker.commands.parser.annotations.JdwpCommandConstructor;
import kaap.veiko.debuggerforker.commands.sets.CommandIdentifier;

@JdwpCommand(CommandIdentifier.ID_SIZES_REPLY)
public class IdSizesReplyCommand implements Command {

  private final IdSizes idSizes;

  @JdwpCommandConstructor
  public IdSizesReplyCommand(int fieldIdSize, int methodIdSize, int objectIdSize, int referenceTypeIdSize, int frameIdSize) {
    this.idSizes = new IdSizes(fieldIdSize, methodIdSize, objectIdSize, referenceTypeIdSize, frameIdSize);
  }

  public IdSizes getIdSizes() {
    return idSizes;
  }

  @Override
  public String toString() {
    return "IdSizesReplyCommand{" +
        "idSizes=" + idSizes +
        '}';
  }
}
