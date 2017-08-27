package kaap.veiko.debuggerforker.commands.parser;

import kaap.veiko.debuggerforker.commands.constants.DataType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface JDWPDataType {
    DataType value();
}
