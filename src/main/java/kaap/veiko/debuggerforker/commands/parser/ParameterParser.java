package kaap.veiko.debuggerforker.commands.parser;

import java.lang.reflect.Parameter;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kaap.veiko.debuggerforker.VMInformation;
import kaap.veiko.debuggerforker.commands.parser.typeparsers.ArrayParser;
import kaap.veiko.debuggerforker.commands.parser.typeparsers.BooleanParser;
import kaap.veiko.debuggerforker.commands.parser.typeparsers.ByteParser;
import kaap.veiko.debuggerforker.commands.parser.typeparsers.DataTypeParser;
import kaap.veiko.debuggerforker.commands.parser.typeparsers.EventKindParser;
import kaap.veiko.debuggerforker.commands.parser.typeparsers.IntParser;
import kaap.veiko.debuggerforker.commands.parser.typeparsers.LocationParser;
import kaap.veiko.debuggerforker.commands.parser.typeparsers.LongParser;
import kaap.veiko.debuggerforker.commands.parser.typeparsers.ShortParser;
import kaap.veiko.debuggerforker.commands.parser.typeparsers.StringParser;
import kaap.veiko.debuggerforker.commands.parser.typeparsers.TypeParser;

public class ParameterParser {

  private final Logger log = LoggerFactory.getLogger(ParameterParser.class);

  private final Set<TypeParser> typeParsers = new HashSet<>();

  public ParameterParser(VMInformation vmInformation) {
    typeParsers.addAll(Arrays.asList(
        new ByteParser(),
        new BooleanParser(),
        new ShortParser(),
        new IntParser(),
        new LongParser(),
        new StringParser(),
        new DataTypeParser(vmInformation),
        new ArrayParser(this),
        new EventKindParser(),
        new LocationParser(vmInformation)
    ));
  }

  public Object[] parseMultipleValuesFromBuffer(ByteBuffer dataBuffer, Parameter[] parameters) throws ReflectiveOperationException {
    Object[] parameterValues = new Object[parameters.length];

    for (int i = 0; i < parameters.length; i++) {
      parameterValues[i] = getParameterValueFromBuffer(dataBuffer, parameters[i]);
    }

    return parameterValues;
  }

  public Object getParameterValueFromBuffer(ByteBuffer dataBuffer, Parameter parameter) throws ReflectiveOperationException {
    TypeParser parser = findParser(parameter.getType());

    if (parser != null) {
      return parser.parse(dataBuffer, parameter);
    }
    else {
      return null;
    }
  }

  public Object getValueFromBuffer(ByteBuffer dataBuffer, Class<?> type) throws ReflectiveOperationException {
    TypeParser parser = findParser(type);

    if (parser != null) {
      return parser.parse(dataBuffer, null);
    }
    else {
      return null;
    }
  }

  private TypeParser findParser(Class<?> type) {
    Set<TypeParser> matchingParsers = typeParsers.stream()
        .filter(parser -> parser.hasCorrectType(type))
        .collect(Collectors.toSet());

    if (matchingParsers.size() != 1) {
      log.error("Failed to find parser for type '{}'", type.getName());
      return null;
    }

    return matchingParsers.iterator().next();
  }

}
