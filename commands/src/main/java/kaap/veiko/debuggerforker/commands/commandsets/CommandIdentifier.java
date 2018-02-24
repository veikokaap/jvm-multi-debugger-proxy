package kaap.veiko.debuggerforker.commands.commandsets;

import static kaap.veiko.debuggerforker.commands.parser.CommandType.COMMAND;
import static kaap.veiko.debuggerforker.commands.parser.CommandType.REPLY;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import kaap.veiko.debuggerforker.commands.parser.CommandType;

public enum CommandIdentifier {

  ID_SIZES_REPLY(1, 7, REPLY),
  DISPOSE_COMMAND(1, 6, COMMAND),
  DISPOSE_REPLY(1, 6, REPLY),

  COMPOSITE_EVENT_COMMAND(64, 100, COMMAND),

  SET_EVENT_REQUEST_COMMAND(15, 1, COMMAND),
  SET_EVENT_REQUEST_REPLY(15, 1, REPLY),
  CLEAR_EVENT_REQUEST_COMMAND(15, 2, COMMAND),
  CLEAR_ALL_BREAKPOINTS_COMMAND(15, 3, COMMAND);

  private final short commandSetId;
  private final short commandId;
  private final CommandType type;

  CommandIdentifier(int commandSetId, int commandId, CommandType type) {
    this.commandSetId = (short) commandSetId;
    this.commandId = (short) commandId;
    this.type = type;
  }

  public static CommandIdentifier of(short commandSet, short command, boolean reply) throws Exception {
    List<CommandIdentifier> identifiers = Arrays.stream(values())
        .filter(ci -> ci.getCommandSetId() == commandSet)
        .filter(ci -> ci.getCommandId() == command)
        .filter(ci -> reply ? ci.getType().equals(REPLY) : ci.getType().equals(COMMAND))
        .collect(Collectors.toList());

    if (identifiers.isEmpty()) {
      throw new Exception("Couldn't find the identifier for commandSet=" + commandSet + ", command=" + command + ", reply=" + reply + ".");
    }

    return identifiers.get(0);
  }

  public short getCommandSetId() {
    return commandSetId;
  }

  public short getCommandId() {
    return commandId;
  }

  public CommandType getType() {
    return type;
  }

  public short getFlags() {
    if (type == REPLY) {
      return -128;
    }
    else {
      return 0;
    }
  }
}