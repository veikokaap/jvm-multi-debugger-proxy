package kaap.veiko.debuggerforker.commands;

import kaap.veiko.debuggerforker.commands.parser.CommandType;
import kaap.veiko.debuggerforker.commands.parser.annotations.JdwpCommand;
import kaap.veiko.debuggerforker.commands.sets.CommandIdentifier;

public abstract class CommandBase implements Command {
  @Override
  public int getCommandSetId() {
    return getCommandIdentifier().getCommandSetId();
  }

  @Override
  public int getCommandId() {
    return getCommandIdentifier().getCommandId();
  }

  @Override
  public boolean isReply() {
    return getCommandIdentifier().getType() == CommandType.REPLY;
  }

  private CommandIdentifier getCommandIdentifier() {
    return this.getClass().getAnnotation(JdwpCommand.class).value();
  }
}
