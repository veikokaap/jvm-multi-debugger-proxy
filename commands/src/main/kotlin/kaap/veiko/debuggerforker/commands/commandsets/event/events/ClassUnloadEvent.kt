package kaap.veiko.debuggerforker.commands.commandsets.event.events

import kaap.veiko.debuggerforker.types.DataReader
import kaap.veiko.debuggerforker.types.DataWriter
import kaap.veiko.debuggerforker.types.jdwp.EventKind

data class ClassUnloadEvent(
        val requestId: Int,
        val signature: String
) : VirtualMachineEvent() {

    override fun write(writer: DataWriter?) = writer!!.run {
        writeType(EventKind.CLASS_UNLOAD)
        writeInt(requestId)
        writeString(signature)
    }

    companion object {
        @JvmStatic
        fun read(reader: DataReader) = ClassUnloadEvent(
                reader.readInt(),
                reader.readString()
        )
    }
}