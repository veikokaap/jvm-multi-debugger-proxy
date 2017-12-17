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
import kaap.veiko.debuggerforker.commands.sets.virtualmachine.IdSizesReply;
import kaap.veiko.debuggerforker.packet.Packet;
import kaap.veiko.debuggerforker.packet.internal.ByteBufferDataReader;
import kaap.veiko.debuggerforker.types.DataType;
import kaap.veiko.debuggerforker.types.VMInformation;

public class CommandDataReader extends ByteBufferDataReader {

  private final static Logger log = LoggerFactory.getLogger(CommandDataReader.class);
  private final Packet packet;

  CommandDataReader(Packet packet, VMInformation vmInformation) {
    super(ByteBuffer.wrap(packet.getDataBytes()), vmInformation);
    this.packet = packet;
  }

  public Packet getPacket() {
    return packet;
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
        return CompositeEventCommand.read(this);
      case CLEAR_ALL_BREAKPOINTS_COMMAND:
        return ClearAllBreakpointsCommand.read(this);
      case CLEAR_EVENT_REQUEST_COMMAND:
        return ClearEventRequestCommand.read(this);
      case SET_EVENT_REQUEST_COMMAND:
        return SetEventRequestCommand.read(this);
      case DISPOSE_COMMAND:
        return DisposeCommand.read(this);
      /* Replies */
      case SET_EVENT_REQUEST_REPLY:
        return SetEventRequestReply.read(this);
      case ID_SIZES_REPLY:
        return IdSizesReply.read(this);
      case DISPOSE_REPLY:
        return DisposeReply.read(this);
      default:
        log.warn("Found CommandIdentifier which doesn't have a rule for creating a command. {}", identifier);
        return null;
    }
  }
}
