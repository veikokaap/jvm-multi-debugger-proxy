package kaap.veiko.debuggerforker.commands.commandsets.event.events

import kaap.veiko.debuggerforker.types.DataReader
import kaap.veiko.debuggerforker.types.DataWriter
import kaap.veiko.debuggerforker.types.jdwp.EventKind
import kaap.veiko.debuggerforker.types.jdwp.Location
import kaap.veiko.debuggerforker.types.jdwp.TaggedObjectId
import kaap.veiko.debuggerforker.types.jdwp.ThreadId

data class ExceptionEvent(
        val requestId: Int,
        val thread: ThreadId,
        val location: Location,
        val exception: TaggedObjectId,
        val catchLocation: Location
) : VirtualMachineEvent() {

    override fun write(writer: DataWriter?) = writer!!.run {
        writeType(EventKind.EXCEPTION)
        writeInt(requestId)
        writeType(thread)
        writeType(location)
        writeType(exception)
        writeType(catchLocation)
    }

    companion object {
        fun read(reader: DataReader) = ExceptionEvent(
                reader.readInt(),
                ThreadId.read(reader),
                Location.read(reader),
                TaggedObjectId.read(reader),
                Location.read(reader)
        )
    }
}