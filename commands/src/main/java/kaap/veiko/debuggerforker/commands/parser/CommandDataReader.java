package kaap.veiko.debuggerforker.commands.parser;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaap.veiko.debuggerforker.commands.Command;
import kaap.veiko.debuggerforker.commands.sets.CommandIdentifier;
import kaap.veiko.debuggerforker.commands.sets.event.CompositeEventCommand;
import kaap.veiko.debuggerforker.commands.sets.eventrequest.ClearAllBreakpointsCommand;
import kaap.veiko.debuggerforker.commands.sets.eventrequest.ClearEventRequestCommand;
import kaap.veiko.debuggerforker.commands.sets.eventrequest.SetEventRequestCommand;
import kaap.veiko.debuggerforker.commands.sets.eventrequest.SetEventRequestReply;
import kaap.veiko.debuggerforker.commands.sets.virtualmachine.IdSizesReplyCommand;
import kaap.veiko.debuggerforker.packet.PacketDataReader;
import kaap.veiko.debuggerforker.types.DataType;
import kaap.veiko.debuggerforker.types.VMInformation;

public class CommandDataReader extends PacketDataReader {

  private final static Logger log = LoggerFactory.getLogger(CommandDataReader.class);

  private final Map<CommandIdentifier, Function<CommandDataReader, Command>> parseMap = new HashMap<>();

  public CommandDataReader(ByteBuffer packetByteBuffer, VMInformation vmInformation) {
    super(packetByteBuffer, vmInformation);
    parseMap.put(CompositeEventCommand.COMMAND_IDENTIFIER, CompositeEventCommand::new);
    parseMap.put(ClearAllBreakpointsCommand.COMMAND_IDENTIFIER, ClearAllBreakpointsCommand::new);
    parseMap.put(ClearEventRequestCommand.COMMAND_IDENTIFIER, ClearEventRequestCommand::new);
    parseMap.put(SetEventRequestCommand.COMMAND_IDENTIFIER, SetEventRequestCommand::new);
    parseMap.put(SetEventRequestReply.COMMAND_IDENTIFIER, SetEventRequestReply::new);
    parseMap.put(IdSizesReplyCommand.COMMAND_IDENTIFIER, IdSizesReplyCommand::new);
  }

  public <T extends DataType> List<T> readList(DataTypeArrayParser<T> parser) {
    try {
      return parser.readList(this);
    }
    catch (Exception e) {
      log.error("Failed to parse array.", e);
      return Collections.emptyList();
    }
  }

  public Command readCommand(CommandIdentifier identifier) {
    return parseMap.get(identifier).apply(this);
  }
}
