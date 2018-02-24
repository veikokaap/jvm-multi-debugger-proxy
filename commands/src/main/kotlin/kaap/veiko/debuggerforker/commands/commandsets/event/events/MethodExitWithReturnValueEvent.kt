package kaap.veiko.debuggerforker.commands.commandsets.event.events

import kaap.veiko.debuggerforker.types.DataReader
import kaap.veiko.debuggerforker.types.DataWriter
import kaap.veiko.debuggerforker.types.jdwp.EventKind
import kaap.veiko.debuggerforker.types.jdwp.Location
import kaap.veiko.debuggerforker.types.jdwp.ThreadId
import kaap.veiko.debuggerforker.types.jdwp.Value

data class MethodExitWithReturnValueEvent(
        val requestId: Int,
        val thread: ThreadId,
        val location: Location,
        val value: Value
) : VirtualMachineEvent() {

    override fun write(writer: DataWriter?) = writer!!.run {
        writeType(EventKind.METHOD_EXIT_WITH_RETURN_VALUE)
        writeInt(requestId)
        writeType(thread)
        writeType(location)
        writeType(value)
    }

    companion object {
        fun read(reader: DataReader) = MethodExitWithReturnValueEvent(
                reader.readInt(),
                ThreadId.read(reader),
                Location.read(reader),
                Value.read(reader)
        )
    }
}