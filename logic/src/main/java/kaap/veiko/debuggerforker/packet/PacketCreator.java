package kaap.veiko.debuggerforker.packet;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;

import kaap.veiko.debuggerforker.types.VMInformation;
import kaap.veiko.debuggerforker.commands.Command;
import kaap.veiko.debuggerforker.commands.parser.ConstructorFinder;
import kaap.veiko.debuggerforker.commands.parser.ParameterParser;

public class PacketCreator {

  private final ConstructorFinder constructorFinder = new ConstructorFinder();
  private final ParameterParser parameterParser;

  public PacketCreator(VMInformation vmInformation) {
    parameterParser = new ParameterParser(vmInformation);
  }

  public Packet createCommand(int id, Command command, boolean fromVirtualMachine) {
    Packet packet = new Packet();

    packet.setId(id);
    packet.setFromVirtualMachine(fromVirtualMachine);
    packet.setCommandSet((short) command.getCommandSetId());
    packet.setCommand((short) command.getCommandId());

    if (command.isReply()) {
      packet.setFlags((short) -128);
    }

    packet.setDataBytes(commandToData(command));
    packet.setLength(Packet.HEADER_LENGTH + packet.getDataBytes().length);

    return packet;
  }

  public byte[] commandToData(Command command) {
    Constructor<? extends Command> constructor = constructorFinder.find(command.getClass());
    Parameter[] parameters = constructor.getParameters();

    return parameterParser.commandToByteArray(command);

  }
}
