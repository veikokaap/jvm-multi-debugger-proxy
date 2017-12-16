package kaap.veiko.debuggerforker.commands.parser;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaap.veiko.debuggerforker.commands.Command;
import kaap.veiko.debuggerforker.commands.sets.CommandIdentifier;
import kaap.veiko.debuggerforker.commands.sets.event.CompositeEventCommand;
import kaap.veiko.debuggerforker.commands.sets.eventrequest.ClearAllBreakpointsCommand;
import kaap.veiko.debuggerforker.commands.sets.eventrequest.ClearEventRequestCommand;
import kaap.veiko.debuggerforker.commands.sets.eventrequest.SetEventRequestCommand;
import kaap.veiko.debuggerforker.commands.sets.eventrequest.SetEventRequestReply;
import kaap.veiko.debuggerforker.commands.sets.virtualmachine.DisposeCommand;
import kaap.veiko.debuggerforker.commands.sets.virtualmachine.DisposeReply;
import kaap.veiko.debuggerforker.commands.sets.virtualmachine.IdSizesReplyCommand;
import kaap.veiko.debuggerforker.packet.Packet;
import kaap.veiko.debuggerforker.packet.internal.PacketDataReader;
import kaap.veiko.debuggerforker.types.DataType;
import kaap.veiko.debuggerforker.types.VMInformation;

public class CommandDataReader extends PacketDataReader {

  private final static Logger log = LoggerFactory.getLogger(CommandDataReader.class);

  CommandDataReader(ByteBuffer packetByteBuffer, VMInformation vmInformation) {
    super(packetByteBuffer, vmInformation);
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

  public Command readCommand(CommandIdentifier identifier, Packet packet) {
    switch (identifier) {
      /* Commands */
      case COMPOSITE_EVENT_COMMAND:
        return new CompositeEventCommand(this, packet);
      case CLEAR_ALL_BREAKPOINTS_COMMAND:
        return new ClearAllBreakpointsCommand(this, packet);
      case CLEAR_EVENT_REQUEST_COMMAND:
        return new ClearEventRequestCommand(this, packet);
      case SET_EVENT_REQUEST_COMMAND:
        return new SetEventRequestCommand(this, packet);
      case DISPOSE_COMMAND:
        return new DisposeCommand(packet);
      /* Replies */
      case SET_EVENT_REQUEST_REPLY:
        return new SetEventRequestReply(this, packet);
      case ID_SIZES_REPLY:
        return new IdSizesReplyCommand(this, packet);
      case DISPOSE_REPLY:
        return new DisposeReply(packet);
      default:
        log.warn("Found CommandIdentifier which doesn't have a rule for creating a command. {}", identifier);
        return null;
    }
  }
}
