package kaap.veiko.debuggerforker.types;

public class UntaggedValue extends Value {
  public UntaggedValue(PacketDataReader reader, byte typeTag) {
    super(reader, typeTag);
  }
}
