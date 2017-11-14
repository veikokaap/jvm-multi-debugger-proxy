package kaap.veiko.debuggerforker.commands.sets.eventrequest.filters;

import kaap.veiko.debuggerforker.commands.parser.annotations.JdwpArray;
import kaap.veiko.debuggerforker.commands.parser.annotations.JdwpSubType;
import kaap.veiko.debuggerforker.commands.types.DataType;

@JdwpArray(counterType = int.class)
@JdwpSubType(identifierAnnotation = EventFilterKind.class)
public abstract class EventRequestFilter implements DataType {
}
