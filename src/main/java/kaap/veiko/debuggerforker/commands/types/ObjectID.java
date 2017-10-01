package kaap.veiko.debuggerforker.commands.types;

import java.nio.ByteBuffer;

import kaap.veiko.debuggerforker.commands.sets.virtualmachine.IDSizesReply;

public class ObjectID extends DataTypeBase {
  public ObjectID(ByteBuffer buffer, IDSizesReply idSizes) {
    super(buffer, idSizes, IDSizesReply::getObjectIDSize);
  }
}
