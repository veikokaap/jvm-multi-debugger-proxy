package kaap.veiko.debuggerforker.commands.parser;

public enum CommandType {
  EITHER,
  REPLY,
  COMMAND;

  public boolean check(boolean reply) {
    if (this == EITHER) {
      return true;
    }
    else if (this == COMMAND) {
      return !reply;
    }
    else {
      return reply;
    }
  }
}
