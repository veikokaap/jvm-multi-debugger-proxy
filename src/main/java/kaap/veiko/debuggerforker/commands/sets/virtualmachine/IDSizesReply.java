package kaap.veiko.debuggerforker.commands.sets.virtualmachine;

import kaap.veiko.debuggerforker.commands.Command;
import kaap.veiko.debuggerforker.commands.parser.annotations.JDWPCommand;
import kaap.veiko.debuggerforker.commands.parser.annotations.JDWPCommandConstructor;
import kaap.veiko.debuggerforker.commands.sets.CommandIdentifier;

@JDWPCommand(CommandIdentifier.ID_SIZES_REPLY)
public class IDSizesReply implements Command {

  private final int fieldIDSize;
  private final int methodIDSize;
  private final int objectIDSize;
  private final int referenceTypeIDSize;
  private final int frameIDSize;

  @JDWPCommandConstructor
  public IDSizesReply(int fieldIDSize, int methodIDSize, int objectIDSize, int referenceTypeIDSize, int frameIDSize) {
    this.fieldIDSize = fieldIDSize;
    this.methodIDSize = methodIDSize;
    this.objectIDSize = objectIDSize;
    this.referenceTypeIDSize = referenceTypeIDSize;
    this.frameIDSize = frameIDSize;
  }

  public int getFieldIDSize() {
    return fieldIDSize;
  }

  public int getMethodIDSize() {
    return methodIDSize;
  }

  public int getObjectIDSize() {
    return objectIDSize;
  }

  public int getReferenceTypeIDSize() {
    return referenceTypeIDSize;
  }

  public int getFrameIDSize() {
    return frameIDSize;
  }

  @Override
  public String toString() {
    return "ID_SIZES_REPLY{" +
        "fieldIDSize=" + fieldIDSize +
        ", methodIDSize=" + methodIDSize +
        ", objectIDSize=" + objectIDSize +
        ", referenceTypeIDSize=" + referenceTypeIDSize +
        ", frameIDSize=" + frameIDSize +
        '}';
  }
}
