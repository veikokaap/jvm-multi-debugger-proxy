package kaap.veiko.debuggerforker.commands.parser.annotations;

import kaap.veiko.debuggerforker.commands.parser.CommandType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface JDWPCommand {
    short commandSet();

    short command();

    CommandType commandType();
}
