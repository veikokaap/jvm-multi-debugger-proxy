package kaap.veiko.debuggerforker.commands.parser.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import kaap.veiko.debuggerforker.commands.sets.CommandIdentifier;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface JDWPCommand {
  CommandIdentifier value();
}
