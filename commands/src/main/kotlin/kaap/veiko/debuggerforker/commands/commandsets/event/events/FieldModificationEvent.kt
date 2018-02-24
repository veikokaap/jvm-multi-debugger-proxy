package kaap.veiko.debuggerforker.commands.commandsets.event.events

import kaap.veiko.debuggerforker.types.DataReader
import kaap.veiko.debuggerforker.types.DataWriter
import kaap.veiko.debuggerforker.types.jdwp.*

data class FieldModificationEvent(
        val requestId: Int,
        val thread: ThreadId,
        val location: Location,
        val refTypeTag: Byte,
        val typeId: ReferenceTypeId,
        val fieldId: FieldId,
        val objectId: TaggedObjectId,
        val valueToBe: Value
) : VirtualMachineEvent() {

    override fun write(writer: DataWriter?) = writer!!.run {
        writeType(EventKind.FIELD_MODIFICATION)
        writeInt(requestId)
        writeType(thread)
        writeType(location)
        writeByte(refTypeTag)
        writeType(typeId)
        writeType(fieldId)
        writeType(objectId)
        writeType(valueToBe)
    }

    companion object {
        fun read(reader: DataReader) = FieldModificationEvent(
                reader.readInt(),
                ThreadId.read(reader),
                Location.read(reader),
                reader.readByte(),
                ReferenceTypeId.read(reader),
                FieldId.read(reader),
                TaggedObjectId.read(reader),
                Value.read(reader)
        )
    }
}