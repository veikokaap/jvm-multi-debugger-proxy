package kaap.veiko.debuggerforker.commands.parser.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import kaap.veiko.debuggerforker.commands.constants.EventKind;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface JdwpSubCommand {
  EventKind eventKind();
}
