package kaap.veiko.debuggerforker.commands.parser;

import java.nio.ByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaap.veiko.debuggerforker.commands.Command;
import kaap.veiko.debuggerforker.commands.commandsets.CommandIdentifier;
import kaap.veiko.debuggerforker.commands.commandsets.event.CompositeEventCommand;
import kaap.veiko.debuggerforker.commands.commandsets.eventrequest.ClearAllBreakpointsCommand;
import kaap.veiko.debuggerforker.commands.commandsets.eventrequest.ClearEventRequestCommand;
import kaap.veiko.debuggerforker.commands.commandsets.eventrequest.ClearEventRequestReply;
import kaap.veiko.debuggerforker.commands.commandsets.eventrequest.SetEventRequestCommand;
import kaap.veiko.debuggerforker.commands.commandsets.eventrequest.SetEventRequestReply;
import kaap.veiko.debuggerforker.commands.commandsets.virtualmachine.DisposeCommand;
import kaap.veiko.debuggerforker.commands.commandsets.virtualmachine.DisposeReply;
import kaap.veiko.debuggerforker.commands.commandsets.virtualmachine.HoldEventsCommand;
import kaap.veiko.debuggerforker.commands.commandsets.virtualmachine.IdSizesReply;
import kaap.veiko.debuggerforker.commands.commandsets.virtualmachine.ReleaseEventsCommand;
import kaap.veiko.debuggerforker.commands.commandsets.virtualmachine.ResumeCommand;
import kaap.veiko.debuggerforker.commands.commandsets.virtualmachine.ResumeReply;
import kaap.veiko.debuggerforker.packet.Packet;
import kaap.veiko.debuggerforker.packet.reader.ByteBufferDataReader;
import kaap.veiko.debuggerforker.types.DataReadException;
import kaap.veiko.debuggerforker.types.VMInformation;

public class CommandDataReader extends ByteBufferDataReader {

  private final static Logger log = LoggerFactory.getLogger(CommandDataReader.class);
  private final Packet packet;

  CommandDataReader(Packet packet, VMInformation vmInformation) {
    super(ByteBuffer.wrap(packet.getData()), vmInformation);
    this.packet = packet;
  }

  public Packet getPacket() {
    return packet;
  }

  public Command readCommand(CommandIdentifier identifier) throws DataReadException {
    switch (identifier) {
      /* Commands */
      case COMPOSITE_EVENT_COMMAND:
        return CompositeEventCommand.read(this);
      case CLEAR_ALL_BREAKPOINTS_COMMAND:
        return ClearAllBreakpointsCommand.read(this);
      case CLEAR_EVENT_REQUEST_COMMAND:
        return ClearEventRequestCommand.read(this);
      case CLEAR_EVENT_REQUEST_REPLY:
        return ClearEventRequestReply.read(this);
      case SET_EVENT_REQUEST_COMMAND:
        return SetEventRequestCommand.read(this);
      case DISPOSE_COMMAND:
        return DisposeCommand.read(this);
      case RESUME_COMMAND:
        return ResumeCommand.read(this);
      case RESUME_REPLY:
        return ResumeReply.read(this);
      case HOLD_EVENTS_COMMAND:
        return HoldEventsCommand.read(this);
      case RELEASE_EVENTS_COMMAND:
        return ReleaseEventsCommand.read(this);
      /* Replies */
      case SET_EVENT_REQUEST_REPLY:
        return SetEventRequestReply.read(this);
      case ID_SIZES_REPLY:
        return IdSizesReply.read(this);
      case DISPOSE_REPLY:
        return DisposeReply.read(this);
      default:
        throw new UnsupportedOperationException("No rule for creating a command for command identifier '" + identifier + "'.");
    }
  }
}
