package kaap.veiko.debuggerforker.types.jdwp;

import kaap.veiko.debuggerforker.types.DataReadException;
import kaap.veiko.debuggerforker.types.DataReader;

public class ThreadGroupId extends ObjectId {
  ThreadGroupId(DataReader reader) throws DataReadException {
    super(reader);
  }

  public ThreadGroupId(long value) {
    super(value);
  }

  public static ThreadGroupId read(DataReader reader) throws DataReadException {
    return new ThreadGroupId(reader);
  }
}
