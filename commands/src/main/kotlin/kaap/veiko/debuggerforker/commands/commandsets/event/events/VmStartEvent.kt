package kaap.veiko.debuggerforker.commands.commandsets.event.events

import kaap.veiko.debuggerforker.types.DataReader
import kaap.veiko.debuggerforker.types.DataWriter
import kaap.veiko.debuggerforker.types.jdwp.EventKind
import kaap.veiko.debuggerforker.types.jdwp.ThreadId

data class VmStartEvent(
        val requestId: Int,
        val thread: ThreadId
) : VirtualMachineEvent() {
    override fun write(writer: DataWriter?) = writer!!.run {
        writeType(EventKind.VM_START)
        writeInt(requestId)
        writeType(thread)
    }

    companion object {
        fun read(reader: DataReader) = VmStartEvent(
                reader.readInt(),
                ThreadId.read(reader)
        )
    }
}