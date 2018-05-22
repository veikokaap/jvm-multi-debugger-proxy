package kaap.veiko.debuggerforker.types.jdwp;

import kaap.veiko.debuggerforker.types.DataReadException;
import kaap.veiko.debuggerforker.types.DataReader;

public class FrameId extends DataTypeBase {
  FrameId(DataReader reader) throws DataReadException {
    super(reader, IdSizes.SizeType.FRAME_ID_SIZE);
  }

  public FrameId(long value) {
    super(value, IdSizes.SizeType.FRAME_ID_SIZE);
  }

  public static FrameId read(DataReader reader) throws DataReadException {
    return new FrameId(reader);
  }
}
