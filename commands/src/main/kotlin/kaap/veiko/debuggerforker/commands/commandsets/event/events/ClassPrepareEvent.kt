package kaap.veiko.debuggerforker.commands.commandsets.event.events

import kaap.veiko.debuggerforker.types.DataReader
import kaap.veiko.debuggerforker.types.DataWriter
import kaap.veiko.debuggerforker.types.jdwp.EventKind
import kaap.veiko.debuggerforker.types.jdwp.ReferenceTypeId
import kaap.veiko.debuggerforker.types.jdwp.ThreadId

data class ClassPrepareEvent(
        val requestId: Int,
        val thread: ThreadId,
        val refTypeTag: Byte,
        val typeId: ReferenceTypeId,
        val signature: String,
        val status: Int
) : VirtualMachineEvent() {

    override fun write(writer: DataWriter?) = writer!!.run {
        writeType(EventKind.CLASS_PREPARE)
        writeInt(requestId)
        writeType(thread)
        writeByte(refTypeTag)
        writeType(typeId)
        writeString(signature)
        writeInt(status)
    }

    companion object {
        fun read(reader: DataReader) = ClassPrepareEvent(
                reader.readInt(),
                ThreadId.read(reader),
                reader.readByte(),
                ReferenceTypeId.read(reader),
                reader.readString(),
                reader.readInt()
        )
    }
}