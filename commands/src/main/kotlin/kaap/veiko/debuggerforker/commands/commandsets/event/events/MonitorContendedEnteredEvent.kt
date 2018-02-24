package kaap.veiko.debuggerforker.commands.commandsets.event.events

import kaap.veiko.debuggerforker.types.DataReader
import kaap.veiko.debuggerforker.types.DataWriter
import kaap.veiko.debuggerforker.types.jdwp.EventKind
import kaap.veiko.debuggerforker.types.jdwp.Location
import kaap.veiko.debuggerforker.types.jdwp.TaggedObjectId
import kaap.veiko.debuggerforker.types.jdwp.ThreadId

data class MonitorContendedEnteredEvent(
        val requestId: Int,
        val thread: ThreadId,
        val objectId: TaggedObjectId,
        val location: Location
) : VirtualMachineEvent() {

    override fun write(writer: DataWriter?) = writer!!.run {
        writeType(EventKind.MONITOR_CONTENDED_ENTERED)
        writeInt(requestId)
        writeType(thread)
        writeType(objectId)
        writeType(location)
    }

    companion object {
        fun read(reader: DataReader) = MonitorContendedEnteredEvent(
                reader.readInt(),
                ThreadId.read(reader),
                TaggedObjectId.read(reader),
                Location.read(reader)
        )
    }
}