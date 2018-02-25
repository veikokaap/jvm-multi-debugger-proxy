package kaap.veiko.debuggerforker.types.jdwp;

import kaap.veiko.debuggerforker.types.DataReader;

public class FrameId extends DataTypeBase {
  public static FrameId read(DataReader reader) {
    return new FrameId(reader);
  }

  FrameId(DataReader reader) {
    super(reader, IdSizes.SizeType.FRAME_ID_SIZE);
  }

  public FrameId(long value) {
    super(value, IdSizes.SizeType.FRAME_ID_SIZE);
  }
}
