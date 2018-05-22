package kaap.veiko.debuggerforker.types.jdwp;

import java.util.Arrays;

import kaap.veiko.debuggerforker.types.DataReadException;
import kaap.veiko.debuggerforker.types.DataReader;
import kaap.veiko.debuggerforker.types.DataType;
import kaap.veiko.debuggerforker.types.DataWriter;

public enum EventKind implements DataType {
  SINGLE_STEP(1),
  BREAKPOINT(2),
  FRAME_POP(3),
  EXCEPTION(4),
  USER_DEFINED(5),
  THREAD_START(6),
  THREAD_DEATH(7),
  CLASS_PREPARE(8),
  CLASS_UNLOAD(9),
  CLASS_LOAD(10),
  FIELD_ACCESS(20),
  FIELD_MODIFICATION(21),
  EXCEPTION_CATCH(30),
  METHOD_ENTRY(40),
  METHOD_EXIT(41),
  METHOD_EXIT_WITH_RETURN_VALUE(42),
  MONITOR_CONTENDED_ENTER(43),
  MONITOR_CONTENDED_ENTERED(44),
  MONITOR_WAIT(45),
  MONITOR_WAITED(46),
  VM_START(90),
  VM_DEATH(99),
  VM_DISCONNECTED(100);

  public final byte id;

  EventKind(int id) {
    this.id = (byte) id;
  }

  public static EventKind read(DataReader reader) throws DataReadException {
    return findByValue(reader.readByte());
  }

  private static EventKind findByValue(byte value) throws DataReadException {
    return Arrays.stream(EventKind.values())
        .filter(eventKind -> eventKind.id == value)
        .findFirst()
        .orElseThrow(() -> new DataReadException("Failed to find EventKind for value '" + value + "'."));
  }

  public byte getId() {
    return id;
  }

  @Override
  public void write(DataWriter writer) {
    writer.writeByte(id);
  }
}
