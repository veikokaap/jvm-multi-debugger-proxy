package kaap.veiko.debuggerforker.commands.commandsets.event.events

import kaap.veiko.debuggerforker.types.DataReader
import kaap.veiko.debuggerforker.types.DataWriter
import kaap.veiko.debuggerforker.types.jdwp.EventKind
import kaap.veiko.debuggerforker.types.jdwp.Location
import kaap.veiko.debuggerforker.types.jdwp.TaggedObjectId
import kaap.veiko.debuggerforker.types.jdwp.ThreadId

data class MonitorWaitEvent(
        val requestId: Int,
        val thread: ThreadId,
        val objectId: TaggedObjectId,
        val location: Location,
        val timeout: Long
) : VirtualMachineEvent() {

    override fun write(writer: DataWriter?) = writer!!.run {
        writeType(EventKind.MONITOR_WAIT)
        writeInt(requestId)
        writeType(thread)
        writeType(objectId)
        writeType(location)
        writeLong(timeout)
    }

    companion object {
        @JvmStatic
        fun read(reader: DataReader) = MonitorWaitEvent(
                reader.readInt(),
                ThreadId.read(reader),
                TaggedObjectId.read(reader),
                Location.read(reader),
                reader.readLong()
        )
    }
}