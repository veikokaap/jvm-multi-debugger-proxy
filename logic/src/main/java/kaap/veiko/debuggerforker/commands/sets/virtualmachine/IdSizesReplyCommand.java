package kaap.veiko.debuggerforker.commands.sets.virtualmachine;

import java.util.Collections;
import java.util.List;

import kaap.veiko.debuggerforker.commands.CommandBase;
import kaap.veiko.debuggerforker.types.IdSizes;
import kaap.veiko.debuggerforker.commands.parser.annotations.JdwpCommand;
import kaap.veiko.debuggerforker.commands.parser.annotations.JdwpCommandConstructor;
import kaap.veiko.debuggerforker.commands.sets.CommandIdentifier;

@JdwpCommand(CommandIdentifier.ID_SIZES_REPLY)
public class IdSizesReplyCommand extends CommandBase {

  private final IdSizes idSizes;

  @JdwpCommandConstructor
  public IdSizesReplyCommand(IdSizes idSizes) {
    this.idSizes = idSizes;
  }

  public IdSizes getIdSizes() {
    return idSizes;
  }

  @Override
  public List<Object> getPacketValues() {
    return Collections.singletonList(idSizes);
  }

  @Override
  public String toString() {
    return "IdSizesReplyCommand{" +
        "idSizes=" + idSizes +
        '}';
  }
}
