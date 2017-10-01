package kaap.veiko.debuggerforker.commands.parser;

public enum CommandType {
  REPLY,
  COMMAND;

  public boolean check(boolean reply) {
    if (this == COMMAND) {
      return !reply;
    }
    else {
      return reply;
    }
  }
}
