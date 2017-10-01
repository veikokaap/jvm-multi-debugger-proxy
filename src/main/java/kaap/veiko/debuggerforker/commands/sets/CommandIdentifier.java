package kaap.veiko.debuggerforker.commands.sets;


import static kaap.veiko.debuggerforker.commands.parser.CommandType.COMMAND;
import static kaap.veiko.debuggerforker.commands.parser.CommandType.REPLY;

import kaap.veiko.debuggerforker.commands.parser.CommandType;

public enum CommandIdentifier {
  ID_SIZES_REPLY(1, 7, REPLY),
  COMPOSITE_EVENT_COMMAND(64, 100, COMMAND);

  private final int commandSetId;
  private final int commandId;
  private final CommandType type;

  CommandIdentifier(int commandSetId, int commandId, CommandType type) {
    this.commandSetId = commandSetId;
    this.commandId = commandId;
    this.type = type;
  }

  public int getCommandSetId() {
    return commandSetId;
  }

  public int getCommandId() {
    return commandId;
  }

  public CommandType getType() {
    return type;
  }
}
