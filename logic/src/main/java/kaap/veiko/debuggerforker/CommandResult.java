package kaap.veiko.debuggerforker;

import kaap.veiko.debuggerforker.commands.Command;

public class CommandResult {
  public static final CommandResult NO_PACKETS_SENT = new CommandResult(false, null);

  public static CommandResult forwardPacket(Command command) {
    return new CommandResult(true, command);
  }

  private final boolean sendPacket;
  private final Command command;

  public CommandResult(boolean sendPacket, Command command) {
    this.sendPacket = sendPacket;
    this.command = command;
  }

  public boolean sendPacket() {
    return sendPacket;
  }

  public Command getCommand() {
    return command;
  }

  @Override
  public String toString() {
    return "CommandResult{" +
        "sendPacket=" + sendPacket +
        ", command=" + command +
        '}';
  }


}
