package kaap.veiko.debuggerforker.types.jdwp;

import kaap.veiko.debuggerforker.types.DataReader;

public class ThreadGroupId extends ObjectId {
  public static ThreadGroupId read(DataReader reader) {
    return new ThreadGroupId(reader);
  }

  ThreadGroupId(DataReader reader) {
    super(reader);
  }

  public ThreadGroupId(long value) {
    super(value);
  }
}
