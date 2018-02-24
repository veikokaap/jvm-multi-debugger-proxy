package kaap.veiko.debuggerforker.commands.commandsets.event.events

import kaap.veiko.debuggerforker.types.DataReader
import kaap.veiko.debuggerforker.types.DataWriter
import kaap.veiko.debuggerforker.types.jdwp.EventKind
import kaap.veiko.debuggerforker.types.jdwp.Location
import kaap.veiko.debuggerforker.types.jdwp.TaggedObjectId
import kaap.veiko.debuggerforker.types.jdwp.ThreadId

data class MonitorWaitedEvent(
        val requestId: Int,
        val thread: ThreadId,
        val objectId: TaggedObjectId,
        val location: Location,
        val timedOut: Boolean
) : VirtualMachineEvent() {

    override fun write(writer: DataWriter?) = writer!!.run {
        writeType(EventKind.MONITOR_WAITED)
        writeInt(requestId)
        writeType(thread)
        writeType(objectId)
        writeType(location)
        writeBoolean(timedOut)
    }

    companion object {
        @JvmStatic
        fun read(reader: DataReader) = MonitorWaitedEvent(
                reader.readInt(),
                ThreadId.read(reader),
                TaggedObjectId.read(reader),
                Location.read(reader),
                reader.readBoolean()
        )
    }
}