package kaap.veiko.debuggerforker.events;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import kaap.veiko.debuggerforker.commands.constants.EventKind;
import kaap.veiko.debuggerforker.commands.parser.annotations.IdentifierType;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@IdentifierType(EventKind.class)
public @interface JdwpEvent {
  EventKind value();
}
