package kaap.veiko.debuggerforker.handlers;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaap.veiko.debuggerforker.ProxyCommandStream;
import kaap.veiko.debuggerforker.commands.commandsets.event.CompositeEventCommand;
import kaap.veiko.debuggerforker.commands.commandsets.event.events.BreakPointEvent;
import kaap.veiko.debuggerforker.commands.commandsets.event.events.ClassPrepareEvent;
import kaap.veiko.debuggerforker.commands.commandsets.event.events.VirtualMachineEvent;
import kaap.veiko.debuggerforker.commands.commandsets.event.events.VmDeathEvent;
import kaap.veiko.debuggerforker.commands.commandsets.event.events.VmStartEvent;
import kaap.veiko.debuggerforker.commands.commandsets.eventrequest.ClearAllBreakpointsCommand;
import kaap.veiko.debuggerforker.commands.commandsets.eventrequest.ClearEventRequestCommand;
import kaap.veiko.debuggerforker.commands.commandsets.eventrequest.ClearEventRequestReply;
import kaap.veiko.debuggerforker.commands.commandsets.eventrequest.SetEventRequestCommand;
import kaap.veiko.debuggerforker.commands.commandsets.eventrequest.SetEventRequestReply;
import kaap.veiko.debuggerforker.packet.CommandPacket;
import kaap.veiko.debuggerforker.packet.PacketSource;
import kaap.veiko.debuggerforker.types.VMInformation;
import kaap.veiko.debuggerforker.types.jdwp.EventKind;

class RequestHandler {

  private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

  private final VMInformation vmInformation;
  private final ProxyCommandStream proxyCommandStream;

  private final ConcurrentMap<CommandPacket, SetEventRequestCommand> setEventRequestCommandMap = new ConcurrentHashMap<>();
  private final ConcurrentMap<RequestIdentifier, List<PacketSource>> eventRequestIdSourceMap = new ConcurrentHashMap<>();
  private final ConcurrentMap<PacketSource, List<Integer>> breakPointRequestIdMap = new ConcurrentHashMap<>();

  RequestHandler(VMInformation vmInformation, ProxyCommandStream proxyCommandStream) {
    this.vmInformation = vmInformation;
    this.proxyCommandStream = proxyCommandStream;
  }

  public void handleSetEventCommand(SetEventRequestCommand command) {
    setEventRequestCommandMap.put(command.getPacket(), command);
    proxyCommandStream.writeToVm(command);
  }

  public void handleCompositeEvent(CompositeEventCommand compositeEventCommand) {
    proxyCommandStream.getAllDebuggers().forEach(source -> sendEventsToSource(source, compositeEventCommand));
  }

  private void sendEventsToSource(PacketSource source, CompositeEventCommand compositeEventCommand) {
    List<VirtualMachineEvent> events = compositeEventCommand.getEvents().stream()
        .filter(event -> isEventRequestedBySource(source, event))
        .collect(Collectors.toList());

    proxyCommandStream.write(
        source,
        CompositeEventCommand.create(proxyCommandStream.getVmSource().createNewOutputId(), compositeEventCommand.getSuspendPolicy(), events, vmInformation)
    );
  }

  //TODO: Fix VirtualMachineEvent and add getEventKind and getRequestId abstract methods
  private boolean isEventRequestedBySource(PacketSource source, VirtualMachineEvent event) {
    if (event instanceof VmStartEvent || event instanceof VmDeathEvent) {
      return true;
    }
    else {
      RequestIdentifier identifier;
      if (event instanceof BreakPointEvent) {
        identifier = new RequestIdentifier(EventKind.BREAKPOINT, ((BreakPointEvent) event).getRequestId());
      }
      else if (event instanceof ClassPrepareEvent) {
        identifier = new RequestIdentifier(EventKind.CLASS_PREPARE, ((ClassPrepareEvent) event).getRequestId());
      }
      else {
        log.warn("TODO at kaap.veiko.debuggerforker.handlers.RequestHandler#isEventRequestedBySource");
        return false;
      }
      List<PacketSource> sources = eventRequestIdSourceMap.get(identifier);
      return sources.contains(source);
    }
  }

  public void handleSetEventReply(SetEventRequestReply reply) {
    RequestIdentifier requestIdentifier = new RequestIdentifier(findCommand(reply).getEventKind(), reply.getRequestId());
    eventRequestIdSourceMap.computeIfAbsent(requestIdentifier, id -> new CopyOnWriteArrayList<>());

    PacketSource originalCommandSource = reply.getPacket().getCommandPacket().getSource();
    eventRequestIdSourceMap.get(requestIdentifier).add(originalCommandSource);
    proxyCommandStream.write(originalCommandSource, reply);

    saveBreakpoint(reply, requestIdentifier, originalCommandSource);
  }

  private void saveBreakpoint(SetEventRequestReply reply, RequestIdentifier requestIdentifier, PacketSource originalCommandSource) {
    SetEventRequestCommand command = findCommand(reply);
    if (command.getEventKind() == EventKind.BREAKPOINT) {
      breakPointRequestIdMap.computeIfAbsent(originalCommandSource, source -> new CopyOnWriteArrayList<>());
      breakPointRequestIdMap.get(originalCommandSource).add(requestIdentifier.getRequestId());
    }
  }

  public void handleClearEventCommand(ClearEventRequestCommand command) {
    RequestIdentifier requestIdentifier = new RequestIdentifier(command.getEventKind(), command.getRequestId());
    List<PacketSource> sources = eventRequestIdSourceMap.get(requestIdentifier);
    sources.remove(command.getSource());

    if (sources.isEmpty()) {
      proxyCommandStream.writeToVm(command);
    }
    else {
      proxyCommandStream.write(command.getSource(), ClearEventRequestReply.create(proxyCommandStream.getVmSource().createNewOutputId()));
    }
  }

  private SetEventRequestCommand findCommand(SetEventRequestReply reply) {
    return setEventRequestCommandMap.get(reply.getPacket().getCommandPacket());
  }

  public void handleClearAllBreakpointsCommand(ClearAllBreakpointsCommand command) {
    PacketSource source = command.getSource();
    List<Integer> breakpointRequestIds = breakPointRequestIdMap.get(source);
    if (breakpointRequestIds != null) {
      for (Integer requestId : breakpointRequestIds) {
        proxyCommandStream.writeToVm(ClearEventRequestCommand.create(source.createNewOutputId(), EventKind.BREAKPOINT, requestId, vmInformation));
      }
    }
  }
}
