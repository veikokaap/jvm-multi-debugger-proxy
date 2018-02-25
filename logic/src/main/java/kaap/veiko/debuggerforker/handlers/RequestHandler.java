package kaap.veiko.debuggerforker.handlers;

import static kaap.veiko.debuggerforker.types.jdwp.EventKind.BREAKPOINT;

import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaap.veiko.debuggerforker.ProxyCommandStream;
import kaap.veiko.debuggerforker.commands.Command;
import kaap.veiko.debuggerforker.commands.commandsets.event.CompositeEventCommand;
import kaap.veiko.debuggerforker.commands.commandsets.event.events.BreakPointEvent;
import kaap.veiko.debuggerforker.commands.commandsets.eventrequest.ClearEventRequestCommand;
import kaap.veiko.debuggerforker.commands.commandsets.eventrequest.SetEventRequestCommand;
import kaap.veiko.debuggerforker.commands.commandsets.eventrequest.SetEventRequestReply;
import kaap.veiko.debuggerforker.commands.commandsets.eventrequest.filters.LocationOnlyEventRequestFilter;
import kaap.veiko.debuggerforker.packet.PacketSource;
import kaap.veiko.debuggerforker.types.VMInformation;
import kaap.veiko.debuggerforker.types.jdwp.Location;

class RequestHandler {

  private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

  private final VMInformation vmInformation;
  private final ProxyCommandStream proxyCommandStream;

  private final ConcurrentMap<Location, Breakpoint> breakpointMap = new ConcurrentHashMap<>();
  private final ConcurrentMap<Integer, Location> packetIdLocationMap = new ConcurrentHashMap<>();

  RequestHandler(VMInformation vmInformation, ProxyCommandStream proxyCommandStream) {
    this.vmInformation = vmInformation;
    this.proxyCommandStream = proxyCommandStream;
  }

  public void handleBreakpointEvent(BreakPointEvent event) {
    Breakpoint breakpoint = breakpointMap.get(event.getLocation());
    if (breakpoint == null) {
      return;
    }

    for (PacketSource source : breakpoint.getSources()) {
      sendBreakPointEventToSource(event, source);
    }
  }

  private void sendBreakPointEventToSource(BreakPointEvent event, PacketSource source) {
    PacketSource vmSource = proxyCommandStream.getVmSource();
    if (vmSource == null) {
      log.error("No VirtualMachine PacketSource");
      return;
    }

    int packetId = vmSource.createNewOutputId();
    CompositeEventCommand eventCommand = CompositeEventCommand.create(
        packetId, (byte) 2, Collections.singletonList(event), vmInformation
    );

    proxyCommandStream.write(source, eventCommand);
  }

  public void handleSetEventReply(SetEventRequestReply reply) {
    if (packetIdLocationMap.containsKey(reply.getPacket().getId())) {
      Location location = packetIdLocationMap.get(reply.getPacket().getId());
      breakpointMap.get(location).setRequestId(reply.getRequestId());
    }

    PacketSource originalCommandSource = reply.getPacket().getCommandPacket().getSource();
    send(originalCommandSource, reply);
  }

  public void handleSetEventCommand(SetEventRequestCommand command) {
    if (command.getEventKind() == BREAKPOINT) {
      Optional<LocationOnlyEventRequestFilter> locationFilter = command.getEventRequestFilters().stream()
          .filter(f -> f instanceof LocationOnlyEventRequestFilter)
          .map(f -> (LocationOnlyEventRequestFilter) f)
          .findFirst();
      if (locationFilter.isPresent()) {
        addLocationFilteredBreakpoint(command, locationFilter.get().getLocation());
      }
      else {
        proxyCommandStream.writeToVm(command);
      }
    }
    else {
      proxyCommandStream.writeToVm(command);
    }
  }

  public void handleClearEventCommand(ClearEventRequestCommand command) {
    if (command.getEventKind() == BREAKPOINT) {
      Optional<Breakpoint> breakpoint = breakpointMap.values().stream()
          .filter(b -> b.getRequestId() == command.getRequestId())
          .findFirst();

      if (breakpoint.isPresent()) {
        removeBreakPointSource(command, breakpoint.get());
      }
      else {
        log.error("Breakpoint with requestId {} not present in breakpoint map", command.getRequestId());
      }
    }
  }

  private void removeBreakPointSource(ClearEventRequestCommand command, Breakpoint breakpoint) {
    breakpoint.getSources().remove(command.getSource());

    // If no debugger has a breakpoint in that location, then remove the breakpoint
    if (breakpoint.getSources().isEmpty()) {
      proxyCommandStream.writeToVm(command);
    }
  }

  private void addLocationFilteredBreakpoint(SetEventRequestCommand command, Location location) {
    if (breakpointMap.containsKey(location)) {
      Breakpoint breakpoint = breakpointMap.get(location);

      int packetId = command.getPacket().getId();
      int requestId = breakpoint.getRequestId();
      send(command.getSource(), SetEventRequestReply.create(packetId, requestId, vmInformation));
    }
    else {
      Breakpoint breakpoint = new Breakpoint(location);
      breakpointMap.put(location, breakpoint);

      breakpoint.getSources().add(command.getSource());
      proxyCommandStream.writeToVm(command);
    }

    packetIdLocationMap.put(command.getPacket().getId(), location);
  }

  private void send(PacketSource source, Command command) {
    proxyCommandStream.write(source, command);
  }
}
