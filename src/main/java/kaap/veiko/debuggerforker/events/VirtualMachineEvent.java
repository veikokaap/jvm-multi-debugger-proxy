package kaap.veiko.debuggerforker.events;

import kaap.veiko.debuggerforker.commands.parser.annotations.JdwpSubType;

@JdwpSubType(identifierAnnotation = JdwpEvent.class)
public abstract class VirtualMachineEvent {
}
