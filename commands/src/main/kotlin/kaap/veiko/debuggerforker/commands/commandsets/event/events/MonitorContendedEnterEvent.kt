package kaap.veiko.debuggerforker.commands.commandsets.event.events

import kaap.veiko.debuggerforker.types.DataReader
import kaap.veiko.debuggerforker.types.DataWriter
import kaap.veiko.debuggerforker.types.jdwp.*

data class MonitorContendedEnterEvent(
        val requestId: Int,
        val thread: ThreadId,
        val objectId: TaggedObjectId,
        val location: Location
) : VirtualMachineEvent() {

    override fun write(writer: DataWriter?) = writer!!.run {
        writeType(EventKind.MONITOR_CONTENDED_ENTER)
        writeInt(requestId)
        writeType(thread)
        writeType(objectId)
        writeType(location)
    }

    companion object {
        fun read(reader: DataReader) = MonitorContendedEnterEvent(
                reader.readInt(),
                ThreadId.read(reader),
                TaggedObjectId.read(reader),
                Location.read(reader)
        )
    }
}