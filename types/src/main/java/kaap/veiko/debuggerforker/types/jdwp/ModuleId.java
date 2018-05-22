package kaap.veiko.debuggerforker.types.jdwp;

import kaap.veiko.debuggerforker.types.DataReadException;
import kaap.veiko.debuggerforker.types.DataReader;

public class ModuleId extends ObjectId {
  ModuleId(DataReader reader) throws DataReadException {
    super(reader);
  }

  public ModuleId(long value) {
    super(value);
  }

  public static ModuleId read(DataReader reader) throws DataReadException {
    return new ModuleId(reader);
  }
}
