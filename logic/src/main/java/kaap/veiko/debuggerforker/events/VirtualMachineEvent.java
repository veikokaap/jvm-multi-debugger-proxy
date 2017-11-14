package kaap.veiko.debuggerforker.events;

import kaap.veiko.debuggerforker.commands.parser.annotations.JdwpArray;
import kaap.veiko.debuggerforker.commands.parser.annotations.JdwpSubType;
import kaap.veiko.debuggerforker.commands.types.DataType;

@JdwpArray(counterType = Integer.class)
@JdwpSubType(identifierAnnotation = JdwpEvent.class)
public abstract class VirtualMachineEvent implements DataType {
}
