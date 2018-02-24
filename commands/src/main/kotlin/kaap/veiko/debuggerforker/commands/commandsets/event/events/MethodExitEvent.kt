package kaap.veiko.debuggerforker.commands.commandsets.event.events

import kaap.veiko.debuggerforker.types.DataReader
import kaap.veiko.debuggerforker.types.DataWriter
import kaap.veiko.debuggerforker.types.jdwp.EventKind
import kaap.veiko.debuggerforker.types.jdwp.Location
import kaap.veiko.debuggerforker.types.jdwp.ThreadId

data class MethodExitEvent(
        val requestId: Int,
        val thread: ThreadId,
        val location: Location
) : VirtualMachineEvent() {

    override fun write(writer: DataWriter?) = writer!!.run {
        writeType(EventKind.METHOD_EXIT)
        writeInt(requestId)
        writeType(thread)
        writeType(location)
    }

    companion object {
        fun read(reader: DataReader) = MethodExitEvent(
                reader.readInt(),
                ThreadId.read(reader),
                Location.read(reader)
        )
    }
}