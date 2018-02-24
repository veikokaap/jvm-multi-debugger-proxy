package kaap.veiko.debuggerforker.commands.commandsets.event.events

import kaap.veiko.debuggerforker.types.DataReader
import kaap.veiko.debuggerforker.types.DataWriter
import kaap.veiko.debuggerforker.types.jdwp.EventKind
import kaap.veiko.debuggerforker.types.jdwp.ThreadId

data class ThreadDeathEvent(
        val requestId: Int,
        val thread: ThreadId
) : VirtualMachineEvent() {

    override fun write(writer: DataWriter?) = writer!!.run {
        writeType(EventKind.THREAD_DEATH)
        writeInt(requestId)
        writeType(thread)
    }

    companion object {
        @JvmStatic
        fun read(reader: DataReader) = ThreadDeathEvent(
                reader.readInt(),
                ThreadId.read(reader)
        )
    }
}