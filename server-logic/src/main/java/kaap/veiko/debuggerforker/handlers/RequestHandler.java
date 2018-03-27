package kaap.veiko.debuggerforker.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import kaap.veiko.debuggerforker.commands.commandsets.virtualmachine.ResumeCommand;
import kaap.veiko.debuggerforker.commands.commandsets.virtualmachine.ResumeReply;
import kaap.veiko.debuggerforker.packet.CommandPacket;
import kaap.veiko.debuggerforker.packet.PacketSource;
import kaap.veiko.debuggerforker.types.VMInformation;
import kaap.veiko.debuggerforker.types.jdwp.EventKind;

class RequestHandler {

  private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

  private final VMInformation vmInformation;
  private final ProxyCommandStream proxyCommandStream;

  private final ConcurrentMap<SetEventRequestCommand, RequestIdentifier> requestIdentifierMap = new ConcurrentHashMap<>();

  private final ConcurrentMap<CommandPacket, SetEventRequestCommand> setEventRequestCommandMap = new ConcurrentHashMap<>();
  private final ConcurrentMap<RequestIdentifier, List<PacketSource>> eventRequestIdSourceMap = new ConcurrentHashMap<>();
  private final ConcurrentMap<PacketSource, List<Integer>> breakPointRequestIdMap = new ConcurrentHashMap<>();

  private final ConcurrentMap<PacketSource, List<VirtualMachineEvent>> suspendedSourceEventMap = new ConcurrentHashMap<>();
  private final ConcurrentMap<VirtualMachineEvent, List<PacketSource>> suspendedEventSourceMap = new ConcurrentHashMap<>();

  RequestHandler(VMInformation vmInformation, ProxyCommandStream proxyCommandStream) {
    this.vmInformation = vmInformation;
    this.proxyCommandStream = proxyCommandStream;
  }

  public void handleSetEventCommand(SetEventRequestCommand command) {
    setEventRequestCommandMap.put(command.getPacket(), command);
    if (requestIdentifierMap.containsKey(command)) {
      handleSetEventReply(createReply(command));
    }
    else {
      proxyCommandStream.writeToVm(command);
    }
  }

  private SetEventRequestReply createReply(SetEventRequestCommand command) {
    RequestIdentifier identifier = requestIdentifierMap.get(command);

    SetEventRequestReply reply = SetEventRequestReply.create(command.getPacket().getId(), identifier.getRequestId(), vmInformation);
    reply.getPacket().setCommandPacket(command.getPacket());
    return reply;
  }

  public void handleCompositeEvent(CompositeEventCommand compositeEventCommand) {
    proxyCommandStream.getAllDebuggers().forEach(source -> sendEventsToSource(source, compositeEventCommand));
  }

  public void handleResume(ResumeCommand command) {
    PacketSource source = command.getSource();

    log.info("Removing events: {}", suspendedSourceEventMap.get(source));
    log.info("Before eventMap {}", suspendedEventSourceMap.entrySet());
    Optional.ofNullable(suspendedSourceEventMap.remove(source))
        .ifPresent(events -> events.forEach(event -> suspendedEventSourceMap.get(event).remove(source)));

    suspendedEventSourceMap.entrySet().stream()
        .filter(entry -> entry.getValue() == null || entry.getValue().isEmpty())
        .map(Map.Entry::getKey)
        .forEach(suspendedEventSourceMap::remove);

    long suspendedSourceCount = suspendedEventSourceMap.values().stream()
        .flatMap(List::stream)
        .distinct()
        .count();

    log.info("Remaining suspended sources count: {}", suspendedSourceCount);
    log.info("Remaining eventMap {}", suspendedEventSourceMap.entrySet());

    if (suspendedSourceCount == 0) {
      proxyCommandStream.writeToVm(command);
    }
    else {
      proxyCommandStream.write(source, ResumeReply.create(command.getPacket().getId()));
    }
  }

  private void sendEventsToSource(PacketSource source, CompositeEventCommand compositeEventCommand) {
    List<VirtualMachineEvent> events = compositeEventCommand.getEvents().stream()
        .filter(event -> isEventRequestedBySource(source, event))
        .collect(Collectors.toList());

    if (events.isEmpty()) {
      return;
    }

    if (compositeEventCommand.getSuspendPolicy() == 2) {
      suspendedSourceEventMap.computeIfAbsent(source, key -> new ArrayList<>());
      events.forEach(event -> suspendedEventSourceMap.computeIfAbsent(event, key -> new ArrayList<>()));

      suspendedSourceEventMap.get(source).addAll(events);
      events.forEach(event -> suspendedEventSourceMap.get(event).add(source));
    }

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
      RequestIdentifier identifier = new RequestIdentifier(event.getEventKind(), event.getRequestId());
      List<PacketSource> sources = eventRequestIdSourceMap.get(identifier);
      return sources.contains(source);
    }
  }

  public void handleSetEventReply(SetEventRequestReply reply) {
    SetEventRequestCommand command = findCommand(reply);
    RequestIdentifier requestIdentifier = new RequestIdentifier(command.getEventKind(), reply.getRequestId());
    requestIdentifierMap.put(command, requestIdentifier);

    eventRequestIdSourceMap.computeIfAbsent(requestIdentifier, id -> new CopyOnWriteArrayList<>());

    PacketSource originalCommandSource = reply.getPacket().getCommandPacket().getSource();
    eventRequestIdSourceMap.get(requestIdentifier).add(originalCommandSource);
    proxyCommandStream.write(originalCommandSource, reply);

    saveBreakpoint(command, requestIdentifier, originalCommandSource);
  }

  private void saveBreakpoint(SetEventRequestCommand command, RequestIdentifier requestIdentifier, PacketSource originalCommandSource) {
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
      requestIdentifierMap.entrySet().stream()
          .filter(entry -> entry.getValue().equals(requestIdentifier))
          .map(Map.Entry::getKey)
          .collect(Collectors.toList())
          .forEach(requestIdentifierMap::remove);

      eventRequestIdSourceMap.remove(requestIdentifier);
      proxyCommandStream.writeToVm(command);
    }
    else {
      proxyCommandStream.write(command.getSource(), ClearEventRequestReply.create(proxyCommandStream.getVmSource().createNewOutputId()));
    }
  }

  private SetEventRequestCommand findCommand(SetEventRequestReply reply) {
    return setEventRequestCommandMap.remove(reply.getPacket().getCommandPacket());
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
