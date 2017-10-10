package kaap.veiko.debuggerforker.commands.constants;

import java.util.Arrays;

public enum Tag {
  ARRAY(91, null),
  BYTE(66, 1),
  CHAR(67, 2),
  OBJECT(76, null),
  FLOAT(70, 4),
  DOUBLE(68, 8),
  INT(73, 4),
  LONG(74, 8),
  SHORT(83, 2),
  VOID(86, 0),
  BOOLEAN(90, 1),
  STRING(115, null),
  THREAD(116, null),
  THREAD_GROUP(103, null),
  CLASS_LOADER(108, null),
  CLASS_OBJECT(99, null);

  private final int id;
  private final Integer size;

  Tag(int value, Integer size) {
    this.id = value;
    this.size = size;
  }

  public static Tag findByValue(byte value) {
    return Arrays.stream(Tag.values())
        .filter(tag -> tag.id == value)
        .findFirst().get();
  }

  public Integer getSize() {
    return size;
  }
}
