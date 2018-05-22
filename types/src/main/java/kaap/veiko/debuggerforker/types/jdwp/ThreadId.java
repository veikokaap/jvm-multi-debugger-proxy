package kaap.veiko.debuggerforker.types.jdwp;

import kaap.veiko.debuggerforker.types.DataReadException;
import kaap.veiko.debuggerforker.types.DataReader;

public class ThreadId extends ObjectId {
  ThreadId(DataReader reader) throws DataReadException {
    super(reader);
  }

  public ThreadId(long value) {
    super(value);
  }

  public static ThreadId read(DataReader reader) throws DataReadException {
    return new ThreadId(reader);
  }
}
