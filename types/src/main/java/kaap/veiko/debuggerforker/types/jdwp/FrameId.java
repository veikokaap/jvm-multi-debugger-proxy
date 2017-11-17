package kaap.veiko.debuggerforker.types.jdwp;

import kaap.veiko.debuggerforker.types.DataReader;

public class FrameId extends DataTypeBase {
  public FrameId(DataReader reader) {
    super(reader, IdSizes::getFrameIdSize);
  }
}
