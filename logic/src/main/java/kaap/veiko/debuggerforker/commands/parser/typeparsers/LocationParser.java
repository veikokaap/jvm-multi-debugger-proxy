package kaap.veiko.debuggerforker.commands.parser.typeparsers;

import java.lang.reflect.Parameter;
import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.VMInformation;
import kaap.veiko.debuggerforker.types.Location;

public class LocationParser implements TypeParser<Location> {

  private final VMInformation vmInformation;

  public LocationParser(VMInformation vmInformation) {
    this.vmInformation = vmInformation;
  }

  @Override
  public boolean hasCorrectType(Class<?> type) {
    return Location.class == type;
  }

  @Override
  public void putToBuffer(ByteBuffer buffer, Location value) {
    value.putToBuffer(buffer);
  }

  @Override
  public Location parse(ByteBuffer byteBuffer, Parameter parameter) throws ReflectiveOperationException {
    return new Location(byteBuffer, vmInformation.getIdSizes());
  }
}
