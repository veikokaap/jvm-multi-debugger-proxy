package kaap.veiko.debuggerforker.commands.sets.eventrequest.filters;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import kaap.veiko.debuggerforker.commands.parser.annotations.IdentifierType;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@IdentifierType(byte.class)
public @interface EventFilterKind {
  byte value();
}
