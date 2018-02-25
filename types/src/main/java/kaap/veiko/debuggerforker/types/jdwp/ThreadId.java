package kaap.veiko.debuggerforker.types.jdwp;

import kaap.veiko.debuggerforker.types.DataReader;

public class ThreadId extends ObjectId {
  public static ThreadId read(DataReader reader) {
    return new ThreadId(reader);
  }

  ThreadId(DataReader reader) {
    super(reader);
  }

  public ThreadId(long value) {
    super(value);
  }
}
