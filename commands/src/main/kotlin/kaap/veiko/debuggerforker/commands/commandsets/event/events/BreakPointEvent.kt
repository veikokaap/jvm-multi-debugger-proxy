package kaap.veiko.debuggerforker.commands.commandsets.event.events

import kaap.veiko.debuggerforker.types.DataReader
import kaap.veiko.debuggerforker.types.DataWriter
import kaap.veiko.debuggerforker.types.jdwp.EventKind
import kaap.veiko.debuggerforker.types.jdwp.Location
import kaap.veiko.debuggerforker.types.jdwp.ThreadId

data class BreakPointEvent(
        val requestId: Int,
        val thread: ThreadId,
        val location: Location
) : VirtualMachineEvent() {

    override fun write(writer: DataWriter?) = writer!!.run {
        writeType(EventKind.BREAKPOINT)
        writeInt(requestId)
        writeType(thread)
        writeType(location)
    }

    companion object {
        fun read(reader: DataReader) = BreakPointEvent(
                reader.readInt(),
                ThreadId.read(reader),
                Location.read(reader)
        )
    }
}